/*
 * Trident - A Multithreaded Server Alternative
 * Copyright 2017 The TridentSDK Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tridentsdk.entity.meta.living.animal;

import net.tridentsdk.base.HorseArmor;
import net.tridentsdk.base.HorseColor;
import net.tridentsdk.base.HorseMarkings;
import net.tridentsdk.base.HorseType;

import java.util.UUID;

/**
 * @author TridentSDK
 * @since 0.5-alpha
 */
// TODO - documentation
public interface HorseMeta extends AnimalMeta {

    boolean isTame();

    void setTame(boolean tame);

    boolean isSaddled();

    void setSaddled(boolean saddled);

    boolean hasChest();

    void setHasChest(boolean hasChest);

    boolean isBred();

    void setBred(boolean isBred);

    boolean isHorseEating();

    void setHorseEating(boolean eating);

    boolean isRearing();

    void setRearing(boolean rearing);

    boolean isMouthOpen();

    void setMouthOpen(boolean mouthOpen);

    HorseType getHorseType();

    void setHorseType(HorseType type);

    HorseColor getHorseColor();

    void setHorseColor(HorseColor color);

    HorseMarkings getHorseMarkings();

    void setHorseMarkings(HorseMarkings markings);

    UUID getOwner();

    void setOwner(UUID uuid);

    HorseArmor getHorseArmor();

    void setHorseArmor(HorseArmor armor);

}
