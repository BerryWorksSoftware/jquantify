/*
 * Copyright 2005-2022 by BerryWorks Software, LLC. All rights reserved.
 *
 *  This file is part of EDIReader. You may obtain a license for its use directly from
 *  BerryWorks Software, and you may also choose to use this software under the terms of the
 *  GPL version 3. Other products in the EDIReader software suite are available only by licensing
 *  with BerryWorks. Only those files bearing the GPL statement below are available under the GPL.
 *
 *  EDIReader is free software: you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  EDIReader is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with EDIReader.  If not, see <http://www.gnu.org/licenses/
 */

package com.berryworks.jquantify.util;

public class TimeScaler {
    private final String mUnits;
    private final float mScale;

    public TimeScaler(float inBase) {
        if (inBase >= 120000) {
            mUnits = "minutes";
            mScale = 60000;
        } else if (inBase >= 2000) {
            mUnits = "seconds";
            mScale = 1000;
        } else {
            mUnits = "ms";
            mScale = 1;
        }
    }

    public String getUnits() {
        return mUnits;
    }

    public float scale(float inValue) {
        return inValue / mScale;
    }

}

