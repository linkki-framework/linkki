/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.samples.treetable.dynamic.pmo;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;
import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;
import org.linkki.samples.treetable.dynamic.model.Player;
import org.linkki.samples.treetable.dynamic.model.Player.Position;

public abstract class CategoryRowPmo<CMO, CPMO extends PlayerTableRowPmo> extends PlayerTableRowPmo
        implements HierarchicalRowPmo<CPMO> {

    private Supplier<Stream<Player>> playerStreamSupplier;
    private SimpleItemSupplier<CPMO, CMO> childRowSupplier;

    public CategoryRowPmo(Supplier<Stream<Player>> playerStreamSupplier,
            Function<Stream<Player>, Stream<CMO>> playersToChildModelObjectMapper,
            Function<CMO, CPMO> childModelObject2pmoMapping) {
        this.playerStreamSupplier = playerStreamSupplier;
        childRowSupplier = new SimpleItemSupplier<>(
                () -> playersToChildModelObjectMapper.apply(playerStreamSupplier.get()).collect(Collectors.toList()),
                childModelObject2pmoMapping);
    }

    @Override
    public String getTeam() {
        return "";
    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public String getFirstName() {
        return "";
    }

    @Override
    public String getLastName() {
        return "";
    }

    @Override
    public String getAge() {
        Double average = playerStreamSupplier.get()
                .map(Player::getDateOfBirth)
                .collect(Collectors.averagingInt(d -> getAge(d)));
        return 0.0d == average ? "" : String.format("Ø %.2f", average);
    }

    @Override
    public List<? extends CPMO> getChildRows() {
        return childRowSupplier.get();
    }

    @Override
    public boolean hasChildRows() {
        return true;
    }

}