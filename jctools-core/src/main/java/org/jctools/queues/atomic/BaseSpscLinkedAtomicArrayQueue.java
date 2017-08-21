/*
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
package org.jctools.queues.atomic;

import static org.jctools.queues.atomic.LinkedAtomicArrayQueueUtil.length;
import static org.jctools.queues.atomic.LinkedAtomicArrayQueueUtil.nextArrayOffset;
import java.lang.reflect.Field;
import java.util.AbstractQueue;
import java.util.Iterator;
import org.jctools.queues.IndexedQueueSizeUtil.IndexedQueue;
import org.jctools.util.PortableJvmInfo;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import org.jctools.queues.MessagePassingQueue;
import org.jctools.queues.MessagePassingQueue.Supplier;
import org.jctools.queues.MessagePassingQueueUtil;
import org.jctools.queues.QueueProgressIndicators;
import org.jctools.queues.IndexedQueueSizeUtil;
import static org.jctools.queues.atomic.LinkedAtomicArrayQueueUtil.*;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.jctools.queues.MpmcArrayQueue;

/**
 * NOTE: This class was automatically generated by org.jctools.queues.atomic.JavaParsingAtomicLinkedQueueGenerator
 * which can found in the jctools-build module. The original source file is BaseSpscLinkedArrayQueue.java.
 */
abstract class BaseSpscLinkedAtomicArrayQueuePrePad<E> extends AbstractQueue<E> implements IndexedQueue {

    long p0, p1, p2, p3, p4, p5, p6, p7;

    long p10, p11, p12, p13, p14, p15;
}

/**
 * NOTE: This class was automatically generated by org.jctools.queues.atomic.JavaParsingAtomicLinkedQueueGenerator
 * which can found in the jctools-build module. The original source file is BaseSpscLinkedArrayQueue.java.
 */
abstract class BaseSpscLinkedAtomicArrayQueueConsumerColdFields<E> extends BaseSpscLinkedAtomicArrayQueuePrePad<E> {

    protected long consumerMask;

    protected AtomicReferenceArray<E> consumerBuffer;
}

/**
 * NOTE: This class was automatically generated by org.jctools.queues.atomic.JavaParsingAtomicLinkedQueueGenerator
 * which can found in the jctools-build module. The original source file is BaseSpscLinkedArrayQueue.java.
 */
abstract class BaseSpscLinkedAtomicArrayQueueConsumerField<E> extends BaseSpscLinkedAtomicArrayQueueConsumerColdFields<E> {

    private static final AtomicLongFieldUpdater<BaseSpscLinkedAtomicArrayQueueConsumerField> C_INDEX_UPDATER = AtomicLongFieldUpdater.newUpdater(BaseSpscLinkedAtomicArrayQueueConsumerField.class, "consumerIndex");

    protected volatile long consumerIndex;

    final void soConsumerIndex(long newValue) {
        C_INDEX_UPDATER.lazySet(this, newValue);
    }

    @Override
    public final long lvConsumerIndex() {
        return consumerIndex;
    }
}

/**
 * NOTE: This class was automatically generated by org.jctools.queues.atomic.JavaParsingAtomicLinkedQueueGenerator
 * which can found in the jctools-build module. The original source file is BaseSpscLinkedArrayQueue.java.
 */
abstract class BaseSpscLinkedAtomicArrayQueueL2Pad<E> extends BaseSpscLinkedAtomicArrayQueueConsumerField<E> {

    long p0, p1, p2, p3, p4, p5, p6, p7;

    long p10, p11, p12, p13, p14, p15, p16, p17;
}

/**
 * NOTE: This class was automatically generated by org.jctools.queues.atomic.JavaParsingAtomicLinkedQueueGenerator
 * which can found in the jctools-build module. The original source file is BaseSpscLinkedArrayQueue.java.
 */
abstract class BaseSpscLinkedAtomicArrayQueueProducerFields<E> extends BaseSpscLinkedAtomicArrayQueueL2Pad<E> {

    private static final AtomicLongFieldUpdater<BaseSpscLinkedAtomicArrayQueueProducerFields> P_INDEX_UPDATER = AtomicLongFieldUpdater.newUpdater(BaseSpscLinkedAtomicArrayQueueProducerFields.class, "producerIndex");

    protected volatile long producerIndex;

    final void soProducerIndex(long newValue) {
        P_INDEX_UPDATER.lazySet(this, newValue);
    }

    @Override
    public final long lvProducerIndex() {
        return producerIndex;
    }
}

/**
 * NOTE: This class was automatically generated by org.jctools.queues.atomic.JavaParsingAtomicLinkedQueueGenerator
 * which can found in the jctools-build module. The original source file is BaseSpscLinkedArrayQueue.java.
 */
abstract class BaseSpscLinkedAtomicArrayQueueProducerColdFields<E> extends BaseSpscLinkedAtomicArrayQueueProducerFields<E> {

    protected long producerBufferLimit;

    // fixed for chunked and unbounded
    protected long producerMask;

    protected AtomicReferenceArray<E> producerBuffer;
}

/**
 * NOTE: This class was automatically generated by org.jctools.queues.atomic.JavaParsingAtomicLinkedQueueGenerator
 * which can found in the jctools-build module. The original source file is BaseSpscLinkedArrayQueue.java.
 */
abstract class BaseSpscLinkedAtomicArrayQueue<E> extends BaseSpscLinkedAtomicArrayQueueProducerColdFields<E> implements MessagePassingQueue<E>, QueueProgressIndicators {

    private static final Object JUMP = new Object();

    @Override
    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int size() {
        return IndexedQueueSizeUtil.size(this);
    }

    @Override
    public final boolean isEmpty() {
        return IndexedQueueSizeUtil.isEmpty(this);
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }

    @Override
    public long currentProducerIndex() {
        return lvProducerIndex();
    }

    @Override
    public long currentConsumerIndex() {
        return lvConsumerIndex();
    }

    protected final void soNext(AtomicReferenceArray<E> curr, AtomicReferenceArray<E> next) {
        int offset = nextArrayOffset(curr);
        soElement(curr, offset, next);
    }

    @SuppressWarnings("unchecked")
    protected final AtomicReferenceArray<E> lvNextArrayAndUnlink(AtomicReferenceArray<E> curr) {
        final int offset = nextArrayOffset(curr);
        final AtomicReferenceArray<E> nextBuffer = (AtomicReferenceArray<E>) lvElement(curr, offset);
        // prevent GC nepotism
        soElement(curr, offset, null);
        return nextBuffer;
    }

    @Override
    public boolean relaxedOffer(E e) {
        return offer(e);
    }

    @Override
    public E relaxedPoll() {
        return poll();
    }

    @Override
    public E relaxedPeek() {
        return peek();
    }

    @Override
    public int drain(Consumer<E> c) {
        return MessagePassingQueueUtil.drain(this, c);
    }

    @Override
    public int fill(Supplier<E> s) {
        // result is a long because we want to have a safepoint check at regular intervals
        long result = 0;
        final int capacity = capacity();
        do {
            final int filled = fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
            if (filled == 0) {
                return (int) result;
            }
            result += filled;
        } while (result <= capacity);
        return (int) result;
    }

    @Override
    public int drain(Consumer<E> c, int limit) {
        return MessagePassingQueueUtil.drain(this, c, limit);
    }

    @Override
    public int fill(Supplier<E> s, int limit) {
        for (int i = 0; i < limit; i++) {
            // local load of field to avoid repeated loads after volatile reads
            final AtomicReferenceArray<E> buffer = producerBuffer;
            final long index = producerIndex;
            final long mask = producerMask;
            final int offset = calcElementOffset(index, mask);
            // expected hot path
            if (index < producerBufferLimit) {
                writeToQueue(buffer, s.get(), index, offset);
            } else {
                if (!offerColdPath(buffer, mask, index, offset, null, s)) {
                    return i;
                }
            }
        }
        return limit;
    }

    @Override
    public void drain(Consumer<E> c, WaitStrategy wait, ExitCondition exit) {
        MessagePassingQueueUtil.drain(this, c, wait, exit);
    }

    @Override
    public void fill(Supplier<E> s, WaitStrategy wait, ExitCondition exit) {
        while (exit.keepRunning()) {
            while (fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) != 0 && exit.keepRunning()) {
                continue;
            }
            int idleCounter = 0;
            while (exit.keepRunning() && fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) == 0) {
                idleCounter = wait.idle(idleCounter);
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation is correct for single producer thread use only.
     */
    @Override
    public boolean offer(final E e) {
        // Objects.requireNonNull(e);
        if (null == e) {
            throw new NullPointerException();
        }
        // local load of field to avoid repeated loads after volatile reads
        final AtomicReferenceArray<E> buffer = producerBuffer;
        final long index = producerIndex;
        final long mask = producerMask;
        final int offset = calcElementOffset(index, mask);
        // expected hot path
        if (index < producerBufferLimit) {
            writeToQueue(buffer, e, index, offset);
            return true;
        }
        return offerColdPath(buffer, mask, index, offset, e, null);
    }

    abstract boolean offerColdPath(AtomicReferenceArray<E> buffer, long mask, long pIndex, int offset, E v, Supplier<? extends E> s);

    /**
     * {@inheritDoc}
     * <p>
     * This implementation is correct for single consumer thread use only.
     */
    @SuppressWarnings("unchecked")
    @Override
    public E poll() {
        // local load of field to avoid repeated loads after volatile reads
        final AtomicReferenceArray<E> buffer = consumerBuffer;
        final long index = consumerIndex;
        final long mask = consumerMask;
        final int offset = calcElementOffset(index, mask);
        // LoadLoad
        final Object e = lvElement(buffer, offset);
        boolean isNextBuffer = e == JUMP;
        if (null != e && !isNextBuffer) {
            // this ensures correctness on 32bit platforms
            soConsumerIndex(index + 1);
            soElement(buffer, offset, null);
            return (E) e;
        } else if (isNextBuffer) {
            return newBufferPoll(buffer, index);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation is correct for single consumer thread use only.
     */
    @SuppressWarnings("unchecked")
    @Override
    public E peek() {
        final AtomicReferenceArray<E> buffer = consumerBuffer;
        final long index = consumerIndex;
        final long mask = consumerMask;
        final int offset = calcElementOffset(index, mask);
        // LoadLoad
        final Object e = lvElement(buffer, offset);
        if (e == JUMP) {
            return newBufferPeek(buffer, index);
        }
        return (E) e;
    }

    final void linkOldToNew(final long currIndex, final AtomicReferenceArray<E> oldBuffer, final int offset, final AtomicReferenceArray<E> newBuffer, final int offsetInNew, final E e) {
        // StoreStore
        soElement(newBuffer, offsetInNew, e);
        // link to next buffer and add next indicator as element of old buffer
        soNext(oldBuffer, newBuffer);
        soElement(oldBuffer, offset, JUMP);
        // index is visible after elements (isEmpty/poll ordering)
        // this ensures atomic write of long on 32bit platforms
        soProducerIndex(currIndex + 1);
    }

    final void writeToQueue(final AtomicReferenceArray<E> buffer, final E e, final long index, final int offset) {
        // StoreStore
        soElement(buffer, offset, e);
        // this ensures atomic write of long on 32bit platforms
        soProducerIndex(index + 1);
    }

    private E newBufferPeek(final AtomicReferenceArray<E> buffer, final long index) {
        AtomicReferenceArray<E> nextBuffer = lvNextArrayAndUnlink(buffer);
        consumerBuffer = nextBuffer;
        final long mask = length(nextBuffer) - 2;
        consumerMask = mask;
        final int offset = calcElementOffset(index, mask);
        // LoadLoad
        return lvElement(nextBuffer, offset);
    }

    private E newBufferPoll(final AtomicReferenceArray<E> buffer, final long index) {
        AtomicReferenceArray<E> nextBuffer = lvNextArrayAndUnlink(buffer);
        consumerBuffer = nextBuffer;
        final long mask = length(nextBuffer) - 2;
        consumerMask = mask;
        final int offset = calcElementOffset(index, mask);
        // LoadLoad
        final E n = lvElement(nextBuffer, offset);
        if (null == n) {
            throw new IllegalStateException("new buffer must have at least one element");
        } else {
            // this ensures correctness on 32bit platforms
            soConsumerIndex(index + 1);
            // StoreStore
            soElement(nextBuffer, offset, null);
            return n;
        }
    }
}
