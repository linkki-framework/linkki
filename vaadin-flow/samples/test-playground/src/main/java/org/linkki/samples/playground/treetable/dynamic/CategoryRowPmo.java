/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.samples.playground.treetable.dynamic;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;
import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;
import org.linkki.samples.playground.treetable.dynamic.Player.Position;

public abstract class CategoryRowPmo<CMO, CPMO extends PlayerTableRowPmo> extends PlayerTableRowPmo
        implements HierarchicalRowPmo<CPMO> {

    private final Supplier<Stream<Player>> playerStreamSupplier;
    private final SimpleItemSupplier<CPMO, CMO> childRowSupplier;

    // tag::hierarchical-row-pmo-with-simple-item-supplier[]
    public CategoryRowPmo(Supplier<Stream<Player>> playerStreamSupplier,
            Function<Stream<Player>, Stream<CMO>> playersToChildModelObjectMapper,
            Function<CMO, CPMO> childModelObject2pmoMapping) {
        this.playerStreamSupplier = playerStreamSupplier;
        childRowSupplier = new SimpleItemSupplier<>(
                () -> playersToChildModelObjectMapper.apply(playerStreamSupplier.get()).collect(Collectors.toList()),
                childModelObject2pmoMapping);
    }
    // end::hierarchical-row-pmo-with-simple-item-supplier[]

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
        var average = playerStreamSupplier.get()
                .map(Player::getDateOfBirth)
                .collect(Collectors.averagingInt(PlayerTableRowPmo::getAge));
        return average <= 0 ? "" : String.format("Ø %.2f", average);
    }

    @Override
    public List<? extends CPMO> getChildRows() {
        return childRowSupplier.get();
    }

}