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
import com.domsplace.Villages.Bases.RawBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Region extends Base {
    public static Region getRegion(Player player) {
        return getRegion(player.getLocation());
    }
    
    public static Region getRegion(DomsLocation player) {
        return getRegion(player.toLocation());
    }
    
    public static Region getRegion(Location location) {
        return getRegion(location.getBlockX(), location.getBlockZ(), location.getWorld());
    }
    
    public static Region getRegion(String location) {
        String[] split = location.split(":");
        if(split.length != 2) return null;
        String world = split[1];
        String[] coords = split[0].split(",");
        if(coords.length != 2) return null;
        if(!isInt(coords[0])) return null;
        if(!isInt(coords[1])) return null;
        int x = getInt(coords[0]);
        int z = getInt(coords[1]);
        
        return new Region (x,z,world);
    }
    
    public static Region getRegion(int x, int z, World world) {
        return getRegion(x, z, world.getName());
    }
    
    public static Region getRegion(int x, int z, String world) {
        x = (int)(Math.floor((double)x / (double)RawBase.regionSize) * (double)RawBase.regionSize);
        z = (int)(Math.floor((double)z / (double)RawBase.regionSize)  * (double)RawBase.regionSize);
        return new Region(x, z, world);
    }
    
    //Instance
    
    //TODO: Store previously made Regions in HashMap
    
    private int x;
    private int z;
    private String world;
    
    private Region(int x, int z, String world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }
    
    public int getX() {return this.x;}
    public int getZ() {return this.z;}
    public int getRegionX() {return (this.x / RawBase.regionSize);}
    public int getRegionZ() {return (this.z / RawBase.regionSize);}
    public String getWorld() {return this.world;}
    //public int getMinX() {return this.x - RawBase.regionSize;}
    //public int getMinZ() {return this.z - RawBase.regionSize;}
    public int getMaxX() {return this.x + RawBase.regionSize - 1;}
    public int getMaxZ() {return this.z + RawBase.regionSize - 1;}
    
    public boolean isWorldLoaded() {return this.getBukkitWorld() != null;}
    
    public boolean compare(Region region) {
        return
            (region.getX() == this.getX()) &&
            (region.getZ() == this.getZ()) &&
            (region.getWorld().equals(this.getWorld()))
        ;
    }
    
    public Region getRelativeRegion(int x, int z) {
        return Region.getRegion(this.x+(x * RawBase.regionSize), this.z+(z * RawBase.regionSize), world);
    }

    public World getBukkitWorld() {
        return Bukkit.getWorld(this.getWorld());
    }

    public Block getLowBlock() {
        World w = this.getBukkitWorld();
        return w.getBlockAt(this.getX(), 64, this.getZ());
    }

    public Block getHighBlock() {
        return this.getBukkitWorld().getBlockAt(this.getMaxX(), 64, this.getMaxZ());
    }
    
    @Override
    public String toString() {
        return this.getX() + "," + this.getZ() + ":" + this.getWorld();
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Region) return this.compare((Region) o);
        return super.equals(o);
    }

    public boolean doesRegionBorder(Region r) {
        if(r.compare(this.getRelativeRegion(0, 1))) return true;
        if(r.compare(this.getRelativeRegion(1, 0))) return true;
        if(r.compare(this.getRelativeRegion(0, -1))) return true;
        if(r.compare(this.getRelativeRegion(-1, 0))) return true;
        
        return false;
    }

    public Location getSafeMiddle() {
        int y = 256;
        int x = this.getX() + (Base.regionSize / 2) - 1;
        int z = this.getZ() + (Base.regionSize / 2) - 1;
        
        Block b = this.getBukkitWorld().getBlockAt(x, y, z);
        Block below;
        Block up;
        Block d = this.getBukkitWorld().getBlockAt(x, 64, z);
        
        boolean look = true;
        
        while(look) {
            below = b.getRelative(0, -1, 0);
            up = b.getRelative(0, 1, 0);
            
            if(b == null) {
                return d.getLocation();
            }
            
            if(below == null) {
                return d.getLocation();
            }
            
            if(up == null) {
                return d.getLocation();
            }
            
            if(below.getY() <= 0) {
                return d.getLocation();
            }
            
            if(b.getType().equals(Material.AIR) && up.getType().equals(Material.AIR) && !below.getType().equals(Material.AIR)) {
                return b.getLocation();
            }
            
            b = b.getRelative(0, -1, 0);
        }
        
        return b.getLocation();
    }
}
