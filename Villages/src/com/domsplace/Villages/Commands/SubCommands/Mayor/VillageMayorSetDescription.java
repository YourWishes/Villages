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
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageMayorSetDescription extends SubCommand {
    public static final String VILLAGE_DESCRIPTION_REGEX = "^[a-zA-Z0-9!@#$^&*(),.\\s]*$";
    public static final int VILLAGE_DESCRIPTION_LENGTH = 80;
    
    public VillageMayorSetDescription() {
        this("mayor");
    }
    
    public VillageMayorSetDescription(String alias) {
        super("village", alias, "set", "description");
        this.setPermission("mayor.setdescription");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(!isPlayer(sender)) {sk(sender, "playeronly");return true;}
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        if(!v.isMayor(r)) {sk(sender, "notmayordescription"); return true;}
        
        if(args.length < 1) {
            sk(sender, "enterdescription");
            return true;
        }
        
        String message = "";
        for(int i = 0; i < args.length; i++) {
            message += args[i];
            if(i < (args.length - 1)) message += " ";
        }
        
        if(message.length() > VILLAGE_DESCRIPTION_LENGTH) {
            sk(sender, "descriptionlong");
            return true;
        }
        
        if(!message.matches(VILLAGE_DESCRIPTION_REGEX)) {
            sk(sender, "invalidvillagedescription");
            return true;
        }
        
        sk(sender, "newdescription", message);
        v.setDescription(message);
        DataManager.saveAll();
        return true;
    }
}
