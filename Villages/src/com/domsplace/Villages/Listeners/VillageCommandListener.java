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

package com.domsplace.Villages.Listeners;

import com.domsplace.Villages.Bases.Base;
import com.domsplace.Villages.Bases.VillageListener;
import com.domsplace.Villages.Events.ResidentAddedEvent;
import com.domsplace.Villages.Events.ResidentRemovedEvent;
import com.domsplace.Villages.Events.VillageCreatedEvent;
import com.domsplace.Villages.Events.VillageDeletedEvent;
import com.domsplace.Villages.Events.VillageExpandEvent;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class VillageCommandListener extends VillageListener {
    private List<String> getCommands(String set, Resident r, Village v) {
        List<String> cmds = Base.getConfig().getStringList("commands." + set);
        List<String> formatted = new ArrayList<String>();
        
        for(String s : cmds) {
            if(r != null) s = s.replaceAll("%p%", r.getName());
            if(v != null) s = s.replaceAll("%v%", v.getName());
            
            formatted.add(s);
        }
        
        return formatted;
    }
    
    private void execute(List<String> cmds) {
        for(String s : cmds) {
            Base.log("Running Village Command \"" + s + "\"");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
        }
    }
    
    //Events
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void handleVillageCreatedCommands(VillageCreatedEvent e) {
        execute(getCommands("village.created", e.getResident(), e.getVillage()));
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void handleVillageDeletedCommands(VillageDeletedEvent e) {
        execute(getCommands("village.deleted", e.getCloser(), e.getVillage()));
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void handleVillagePlayerAddedCommands(ResidentAddedEvent e) {
        execute(getCommands("village.playeradded", e.getResident(), e.getVillage()));
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void handleVillagePlayerRemovedCommands(ResidentRemovedEvent e) {
        execute(getCommands("village.playerremoved", e.getResident(), e.getVillage()));
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void handleVillageExpandedCommands(VillageExpandEvent e) {
        execute(getCommands("village.expand", e.getExpander(), e.getVillage()));
    }
}
