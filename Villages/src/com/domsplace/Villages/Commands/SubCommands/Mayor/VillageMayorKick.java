/*
 * Copyright 2013 Dominic Masters and Jordan Atkins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.domsplace.Villages.Commands.SubCommands.Mayor;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Events.ResidentRemovedEvent;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageMayorKick extends SubCommand {
    public VillageMayorKick() {
        this("mayor");
    }
    
    public VillageMayorKick(String alias) {
        super("village", alias, "kick");
        this.setPermission("mayor.kick");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {sk(sender, "playeronly");return true;}
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        if(!v.isMayor(r)) {sk(sender, "mayorkickonly"); return true;}
        
        if(args.length < 1) {
            sk(sender, "enterkickname");
            return true;
        }
        
        Resident target = Resident.guessResident(args[0]);
        if(target == null) {
            sk(sender, "playernotfound");
            return true;
        }
        
        if(!v.isResident(target)) {
            sk(sender, "notresident", target);
            return true;
        }
        
        if(v.isMayor(target)) {
            sk(sender, "cantkickmayor");
            return true;
        }
        
        ResidentRemovedEvent event = new ResidentRemovedEvent(target, v);
        event.fireEvent();
        if(event.isCancelled()) return true;
        
        v.broadcast(gk("residentkicked", target));
        v.removeResident(target);
        DataManager.saveAll();
        return true;
    }
}
