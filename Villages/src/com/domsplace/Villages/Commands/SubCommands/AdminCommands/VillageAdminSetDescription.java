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

import static com.domsplace.Villages.Bases.Base.sk;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Commands.SubCommands.Mayor.VillageMayorSetDescription;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageAdminSetDescription extends SubCommand {
    public VillageAdminSetDescription() {
        super("village", "admin", "set", "description");
        this.setPermission("admin.setdescription");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sk(sender, "needvillagename");
            return false;
        }
        
        if(args.length < 2) {
            sk(sender, "enterdescription");
            return false;
        }
        
        Village v = Village.getVillage(args[0]);
        
        if(v == null) {sk(sender, "villagedoesntexist");return true;}
        
        String message = "";
        for(int i = 1; i < args.length; i++) {
            message += args[i];
            if(i < (args.length - 1)) message += " ";
        }
        
        if(message.length() > VillageMayorSetDescription.VILLAGE_DESCRIPTION_LENGTH) {
            sk(sender, "descriptionlong");
            return true;
        }
        
        if(!message.matches(VillageMayorSetDescription.VILLAGE_DESCRIPTION_REGEX)) {
            sk(sender, "invalidvillagedescription");
            return true;
        }
        
        sk(sender, "newdescription", message);
        v.setDescription(message);
        DataManager.saveAll();
        return true;
    }
}
