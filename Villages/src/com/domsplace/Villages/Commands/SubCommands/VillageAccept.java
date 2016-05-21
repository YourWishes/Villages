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

package com.domsplace.Villages.Commands.SubCommands;

import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Events.ResidentAddedEvent;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageAccept extends SubCommand {
    public VillageAccept() {
        super("village", "accept");
        this.setPermission("accept");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {
            sk(sender, "playeronly");
            return true;
        }
        
        Resident r = Resident.getResident(getPlayer(sender));
        if(r == null) {
            sk(sender, "noinvite");
            return true;
        }
        
        Map<Resident, Village> invites = VillageInvite.VILLAGE_INVITES;
        if(invites == null) {
            sk(sender, "noinvite");
            return true;
        }
        
        if(!invites.containsKey(r))  {
            sk(sender, "noinvite");
            return true;
        }
        
        Village v = invites.get(r);
        if(v == null) {
            sk(sender, "noinvite");
            return true;
        }
        
        if(Village.getPlayersVillage(r) != null) {
            sendMessage(r, "alreadyinvillage");
            return true;
        }
        
        invites.remove(r);
        
        ResidentAddedEvent event = new ResidentAddedEvent(r, v);
        event.fireEvent();
        if(event.isCancelled()) return true;
        
        v.addResident(r);
        v.broadcast(gk("joinedvillage", r));
        DataManager.saveAll();
        return true;
    }

    @Override public String getHelpTextShort(String label) {return "Accept a recieved Village Invite";}
}
