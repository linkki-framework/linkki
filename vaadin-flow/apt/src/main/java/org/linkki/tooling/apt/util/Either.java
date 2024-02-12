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

package org.linkki.tooling.apt.util;

import java.util.Optional;
import java.util.function.Function;

/**
 * A value that is either the left or the right value.
 */
public interface Either<L, R> {

    /**
     * 
     * Transform the left value if present.
     * 
     * @param transform a transformation to transform the left value.
     * @return If this object is an instance of {@link Left}, then the value will be transformed and
     *         a new Either of {@link Left} will be returned, else the instance will only be casted
     *         to fit the return type.
     */
    <U> Either<U, R> mapLeft(Function<L, U> transform);

    /**
     * Transform the right value if present.
     * 
     * @param transform a transformation to transform the right value.
     * @return If this object is an instance of {@link Right}, then the value will be transformed
     *         and a new Either of {@link Right} will be returned, else the instance will only be
     *         casted to fit the return type.
     */
    <U> Either<L, U> mapRight(Function<R, U> transform);

    <U> Either<U, R> flatMapLeft(Function<L, Either<U, R>> transform);

    <U> Either<L, U> flatMapRight(Function<R, Either<L, U>> transform);

    boolean isLeft();

    boolean isRight();

    Optional<L> getLeft();

    Optional<R> getRight();

    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    static <T, L extends T, R extends T> T get(Either<L, R> either) {
        if (either.isLeft()) {
            return ((Left<L, R>)either).getValue();
        } else if (either.isRight()) {
            return ((Right<L, R>)either).getValue();
        } else {
            throw new IllegalArgumentException("unknown type " + either.getClass());
        }
    }

    class Left<L, R> implements Either<L, R> {

        private final L value;

        public Left(L value) {
            this.value = value;
        }

        public L getValue() {
            return value;
        }

        @Override
        public <U> Either<U, R> mapLeft(Function<L, U> transform) {
            return Either.left(transform.apply(getValue()));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> Either<L, U> mapRight(Function<R, U> transform) {
            return (Either<L, U>)this;
        }

        @Override
        public <U> Either<U, R> flatMapLeft(Function<L, Either<U, R>> transform) {
            return transform.apply(getValue());
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> Either<L, U> flatMapRight(Function<R, Either<L, U>> transform) {
            return (Either<L, U>)this;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public Optional<L> getLeft() {
            return Optional.of(value);
        }

        @Override
        public Optional<R> getRight() {
            return Optional.empty();
        }

    }

    class Right<L, R> implements Either<L, R> {

        private final R value;

        public Right(R value) {
            this.value = value;
        }

        public R getValue() {
            return value;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> Either<U, R> mapLeft(Function<L, U> transform) {
            return (Either<U, R>)this;
        }

        @Override
        public <U> Either<L, U> mapRight(Function<R, U> transform) {
            return Either.right(transform.apply(getValue()));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> Either<U, R> flatMapLeft(Function<L, Either<U, R>> transform) {
            return (Either<U, R>)this;
        }

        @Override
        public <U> Either<L, U> flatMapRight(Function<R, Either<L, U>> transform) {
            return transform.apply(getValue());
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public Optional<L> getLeft() {
            return Optional.empty();
        }

        @Override
        public Optional<R> getRight() {
            return Optional.of(value);
        }

    }
}
