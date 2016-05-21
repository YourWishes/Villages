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

package com.domsplace.Villages.Objects;

import com.domsplace.Villages.Bases.Base;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubCommandOption extends Base {
    public static final SubCommandOption PLAYERS_OPTION = new SubCommandOption("[PLAYER]");
    public static final SubCommandOption VILLAGES_OPTION = new SubCommandOption("[VILLAGE]");
    public static final SubCommandOption RESIDENTS_OPTION = new SubCommandOption("[RESIDENTS]");
    public static final SubCommandOption TAXES_OPTION = new SubCommandOption("[TAXES]");
    
    //Instance
    private String option;
    private List<SubCommandOption> subOptions;
    
    public SubCommandOption(String option) {
        this.option = option;
        this.subOptions = new ArrayList<SubCommandOption>();
    }
    
    public SubCommandOption(String option, SubCommandOption... options) {
        this(option);
        this.subOptions.addAll(Arrays.asList(options));
    }
    
    public SubCommandOption(String option, String... options) {
        this(option);
        for(String s : options) {
            this.subOptions.add(new SubCommandOption(s));
        }
    }
    
    public SubCommandOption(SubCommandOption option, Object... objects) {
        this(option.option, objects);
    }
    
    public SubCommandOption(String option, Object... objects) {
        this(option);
        for(Object o : objects) {
            if(o instanceof SubCommandOption) {
                this.subOptions.add((SubCommandOption) o);
                continue;
            }
            this.subOptions.add(new SubCommandOption(o.toString()));
        }
    }
    
    public SubCommandOption(SubCommandOption option, SubCommandOption... options) {
        this(option.option, options);
    }
    
    public SubCommandOption(SubCommandOption option, String... options) {
        this(option.option, options);
    }
    
    public String getOption() {return this.option;}
    public List<SubCommandOption> getSubCommandOptions() {return new ArrayList<SubCommandOption>(this.subOptions);}

    public List<String> getOptionsFormatted(CommandSender sender) {
        List<String> returnV = new ArrayList<String>();
        if(this.compare(SubCommandOption.PLAYERS_OPTION)) {
            for(Player p : Base.getOnlinePlayers(sender)) {
                returnV.add(p.getName());
            }
        } else if(this.compare(SubCommandOption.VILLAGES_OPTION)) {
            for(Village v : Village.getVillages()) {
                returnV.add(v.getName());
            }
        } else if(this.compare(SubCommandOption.RESIDENTS_OPTION)) {
            Village v = Village.getPlayersVillage(Resident.getResident(sender));
            if(v != null) {
                returnV.addAll(v.getResidentsAsString());
            }
        } else if(this.compare(SubCommandOption.TAXES_OPTION)) {
            for(Tax t : Tax.getTaxes()) {
                returnV.add(t.getName());
            }
        } else {
            returnV.add(this.option);
        }
        return returnV;
    }
    
    public static String reverse(String s, CommandSender sender) {
        if(Base.getPlayer(sender, s) != null) return SubCommandOption.PLAYERS_OPTION.option;
        if(Village.getVillage(s) != null) return SubCommandOption.VILLAGES_OPTION.option;
        if(Resident.guessResident(s) != null) return SubCommandOption.RESIDENTS_OPTION.option;
        if(Tax.getTaxByName(s) != null) return SubCommandOption.TAXES_OPTION.option;
        return s;
    }
    
    public List<String> getOptionsAsStringList(CommandSender sender) {
        List<String> returnV = new ArrayList<String>();
        for(SubCommandOption sc : this.subOptions) {
            returnV.addAll(sc.getOptionsFormatted(sender));
        }
        
        return returnV;
    }
    
    public List<String> tryFetch(String[] args, int lvl, CommandSender sender) {
        List<String> opts = new ArrayList<String>();
        
        lvl = lvl + 1;
        int targetLevel = args.length;
        
        if(targetLevel > lvl) {
            for(SubCommandOption sco : this.subOptions) {
                String s = args[lvl-1].toLowerCase();
                s = reverse(s, sender);
                if(!sco.getOption().toLowerCase().startsWith(s.toLowerCase())) continue;
                opts.addAll(sco.tryFetch(args, lvl, sender));
            }
        } else {
            return this.getOptionsAsStringList(sender);
        }
        
        return opts;
    }
    
    public boolean compare(SubCommandOption option) {
        if(option.getOption().equalsIgnoreCase(this.getOption())) return true;
        return false;
    }
}
