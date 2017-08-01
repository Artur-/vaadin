/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.server;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A wrapper for reentrant locks which do not extend {@link ReentrantLock}.
 * <p>
 * Provides method needed by the VaadinSession lock mechanism.
 *
 * @since
 */
public class ReentrantLockWrapper implements Lock {

    private final Lock lock;
    /**
     * The thread which is holding the lock or <code>null</code> if no thread is
     * holding the lock.
     */
    private Thread thread;
    /**
     * Number of times the current thread has locked the lock.
     */
    private int holdCount;

    /**
     * Creates a wrapper for the given lock.
     *
     * @param lock
     *            the lock to wrap, not <code>null</code>
     */
    public ReentrantLockWrapper(Lock lock) {
        assert lock != null;
        this.lock = lock;
        holdCount = 0;
        thread = null;
    }

    @Override
    public void lock() {
        lock.lock();
        holdCount++;
        thread = Thread.currentThread();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        lock.lockInterruptibly();
        holdCount++;
        thread = Thread.currentThread();
    }

    @Override
    public boolean tryLock() {
        if (lock.tryLock()) {
            holdCount++;
            thread = Thread.currentThread();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit)
            throws InterruptedException {
        if (lock.tryLock(time, unit)) {
            holdCount++;
            thread = Thread.currentThread();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void unlock() {
        assert isHeldByCurrentThread();
        lock.unlock();
        holdCount--;
        if (holdCount == 0) {
            thread = null;
        }

    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Conditions are not supported");
    }

    /**
     * Queries if this lock is held by the current thread.
     *
     * <p>
     * Analogous to the {@link Thread#holdsLock(Object)} method for built-in
     * monitor locks, this method is typically used for debugging and testing.
     *
     * @return {@code true} if current thread holds this lock and {@code false}
     *         otherwise
     */
    public boolean isHeldByCurrentThread() {
        return thread == Thread.currentThread();
    }

    /**
     * Queries the number of holds on this lock by the current thread.
     * <p>
     * A thread has a hold on a lock for each lock action that is not matched by
     * an unlock action.
     * <p>
     * The hold count information is typically only used for testing and
     * debugging purposes.
     *
     * @return the number of holds on this lock by the current thread, or zero
     *         if this lock is not held by the current thread
     */
    public int getHoldCount() {
        return holdCount;
    }

}
