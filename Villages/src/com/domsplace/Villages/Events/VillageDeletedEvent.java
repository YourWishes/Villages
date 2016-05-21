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

package com.domsplace.Villages.Events;

import com.domsplace.Villages.Bases.CancellableEvent;
import com.domsplace.Villages.Enums.DeleteCause;
import com.domsplace.Villages.Objects.Resident;
import com.domsplace.Villages.Objects.Village;

public class VillageDeletedEvent extends CancellableEvent {
    private Village village;
    private DeleteCause cause;
    private Resident closer;
    
    public VillageDeletedEvent(Village village, DeleteCause cause, Resident closer) {
       this.village = village;
       this.cause = cause;
       this.closer = closer;
    }
    
    public Village getVillage() {return this.village;}
    public DeleteCause getCause() {return this.cause;}
    public Resident getCloser() {return this.closer;}
}
