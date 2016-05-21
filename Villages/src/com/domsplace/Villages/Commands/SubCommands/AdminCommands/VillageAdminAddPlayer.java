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

package com.domsplace.Villages.Commands.SubCommands.AdminCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Events.ResidentAddedEvent;
import com.domsplace.Villages.Events.ResidentRemovedEvent;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageAdminAddPlayer extends SubCommand {
    public VillageAdminAddPlayer() {
        super("village", "admin", "add", "player");
        this.setPermission("admin.addplayer");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sk(sender, "needvillagename");
            return false;
        }
        
        if(args.length < 2) {
            sk(sender, "enterplayer");
            return false;
        }
        
        Village v = Village.getVillage(args[0]);
        if(v == null) {
            sk(sender, "villagedoesntexist");
            return true;
        }
        
        Resident player = Resident.guessResident(args[1]);
        if(player == null) {
            sk(sender, "playernotfound");
            return true;
        }
        
        /*Village residentVillage = Village.getPlayersVillage(player);
        if(residentVillage == null) {
            sk(sender, "playernotinvillage");
            return true;
        }
        
        if(!residentVillage.equals(v)) {
            sk(sender, "playerdifferentvillage");
            return true;
        }
        
        v.setMayor(player);*/
        
        Village residentVillage = Village.getPlayersVillage(player);
        if(residentVillage != null) {
            if(residentVillage.isMayor(player)) {
                sk(sender, "cantkickmayor");
                return true;
            }
        
            ResidentRemovedEvent event = new ResidentRemovedEvent(player, residentVillage);
            event.fireEvent();
            if(event.isCancelled()) return true;

            residentVillage.removeResident(player);
            sk(sender, "playerremovedfromvillage", player, v);
        }
        
        v.addResident(player);
        
        ResidentAddedEvent event2 = new ResidentAddedEvent(player, v);
        event2.fireEvent();
        if(event2.isCancelled()) return true;
        
        sk(sender, "playeraddedtovillage", player, v);
        DataManager.saveAll();
        return true;
    }
}
