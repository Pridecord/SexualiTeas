/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of Identities.
 *
 * Identities is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Identities is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Identities.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.lucypoulton.identities.storage;

import com.google.common.collect.SetMultimap;

import java.util.Set;
import java.util.UUID;

public interface Storage {
    Set<String> getIdentities(UUID uuid);
    void setIdentities(UUID uuid, Set<String> set);
    void clearIdentities(UUID uuid);
    SetMultimap<UUID, String> getAllIdentities();
}
