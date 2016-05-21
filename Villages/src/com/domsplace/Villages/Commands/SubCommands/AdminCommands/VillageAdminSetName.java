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
import com.domsplace.Villages.Commands.SubCommands.VillageCreate;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageAdminSetName extends SubCommand {
    public VillageAdminSetName() {
        super("village", "admin", "set", "name");
        this.setPermission("admin.setname");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1) {
            sk(sender, "needvillagename");
            return false;
        }
        
        if(args.length < 2) {
            sk(sender, "needvillagename");
            return false;
        }
        
        String name = args[1];
        if(name.length() >= VillageCreate.VILLAGE_NAME_LENGTH || name.length() < 3) {
            sk(sender, "invalidvillagename");
            return true;
        }
        
        if(!name.matches(VillageCreate.VILLAGE_NAME_REGEX)) {
            sk(sender, "invalidvillagename");
            return true;
        }
        
        if(Village.getVillage(name) != null) {
            sk(sender, "villagenameused");
            return true;
        }
        
        Village v = Village.getVillage(args[0]);
        if(v == null) {
            sk(sender, "villagedoesntexist");
            return true;
        }
        
        DataManager.VILLAGE_MANAGER.changeVillageName(v, name);
        v.setName(name);
        sk(sender, "villagenamechaned", v);
        DataManager.saveAll();
        return true;
    }
}
