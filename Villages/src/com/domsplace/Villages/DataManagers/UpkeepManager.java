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

package com.domsplace.Villages.DataManagers;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.DataManager;
import com.domsplace.Villages.Enums.ManagerType;
import com.domsplace.Villages.Enums.TaxMultiplierType;
import com.domsplace.Villages.Exceptions.InvalidItemException;
import com.domsplace.Villages.Objects.Tax;
import com.domsplace.Villages.Objects.DomsItem;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class UpkeepManager extends DataManager {
    private YamlConfiguration upkeep;
    private File upkeepFile;
    
    public UpkeepManager() {
        super(ManagerType.UPKEEP);
    }
    
    
    @Override
    public void tryLoad() throws IOException {
        this.upkeepFile = new File(getDataFolder(), "upkeep.yml");
        
        boolean create = false;
        
        if(!this.upkeepFile.exists()) {
            create = true;
            upkeepFile.createNewFile();
        }
        this.upkeep = YamlConfiguration.loadConfiguration(upkeepFile);
        
        /*** GENERATE DEFAULT CONFIG ***/
        if(create) {
            this.upkeep.set("maintax.message", "&eTax man has visited!");
            this.upkeep.set("maintax.hours", 24);
            this.upkeep.set("maintax.money", 100d);
            this.upkeep.set("maintax.multiplier.type", "player");
            this.upkeep.set("maintax.multiplier.amount", 2.00d);
            
            List<String> items = new ArrayList<String>();
            
            items.add("{size:\"20\"}," + new DomsItem(Material.WOOD).toString());
            items.add("{size:\"10\"}," + new DomsItem(Material.BREAD).toString());
            
            this.upkeep.set("maintax.items", items);
        }
        
        //Reset Taxes
        Tax.deRegsiterTaxes();
        
        //Load Taxes
        for(String key : this.upkeep.getKeys(false)) {
            String message = Base.colorise(upkeep.getString(key + ".message", "&eTax day has arrived!"));
            double hours = upkeep.getDouble(key + ".hours", 24d);
            double money = upkeep.getDouble(key + ".money", 100d);
            
            TaxMultiplierType type;
            
            if(upkeep.getString(key + ".multiplier.type", "player").equalsIgnoreCase("player")) {
                type = TaxMultiplierType.PLAYER;
            } else {
                type = TaxMultiplierType.CHUNK;
            }
            
            double mult = upkeep.getDouble(key + ".multiplier.amount", 2d);
            List<DomsItem> items;
            
            try {
                items = DomsItem.createAllItems(upkeep.getStringList(key + ".items"));
            } catch(InvalidItemException e) {
                log("Tax Data for " + key + " contains an invalid item! \"" + e.getItemData() + "\"");
                items = new ArrayList<DomsItem>();
            }
            
            Tax t = new Tax(key, message, hours, money, type, mult, items);
        }
        
        this.trySave();
    }
    
    @Override
    public void trySave() throws IOException {
        this.upkeep.save(upkeepFile);
    }
}
