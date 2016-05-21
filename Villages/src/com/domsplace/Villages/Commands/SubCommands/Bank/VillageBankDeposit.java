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

package com.domsplace.Villages.Commands.SubCommands.Bank;

import com.domsplace.Villages.Bases.Base;
import static com.domsplace.Villages.Bases.Base.getPlayer;
import static com.domsplace.Villages.Bases.Base.sk;
import com.domsplace.Villages.Bases.BukkitCommand;
import com.domsplace.Villages.Bases.PluginHook;
import com.domsplace.Villages.Bases.SubCommand;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VillageBankDeposit extends SubCommand {
    public VillageBankDeposit() {
        super("village", "bank", "deposit");
        this.setPermission("bank.deposit");
    }
    
    @Override
    public boolean cmd(BukkitCommand bkcmd, CommandSender sender, Command cmd, String label, String[] args) {
        //Make sure it's a player running the command
        if(!isPlayer(sender)) {
            sk(sender, "playeronly");
            return false;
        }
        
        Resident r = Resident.getResident(getPlayer(sender));
        Village v = Village.getPlayersVillage(r);
        if(v == null) {sk(sender, "notinvillage");return true;}
        
        if(args.length < 1) {
            sk(sender, "enteramount");
            return false;
        }
        
        if(!isDouble(args[0])) {
            sk(sender, "notmoney");
            return false;
        }
        
        double amt = getDouble(args[0]);
        if(amt <= 0) {
            sk(sender, "mustbeone");
            return true;
        }
        
        double b = Base.getBalance(sender.getName());
        if(b < amt) {
            sk(sender, "notenoughmoney", PluginHook.VAULT_HOOK.formatEconomy(amt));
            return true;
        }
        
        PluginHook.VAULT_HOOK.getEconomy().withdrawPlayer(sender.getName(), amt);
        v.broadcast(gk("depositedmoney", r, PluginHook.VAULT_HOOK.formatEconomy(amt)));
        v.getBank().addWealth(amt);
        return true;
    }
}
