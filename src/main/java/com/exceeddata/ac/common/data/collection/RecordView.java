package com.exceeddata.ac.common.data.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.exceeddata.ac.common.data.record.Record;

/**
 * <code>RecordView</code> defines the specification for a view of records.
 */
public final class RecordView implements RecordWindow {
    private static final long serialVersionUID = 1L;
    
    private RecordCollection collection = null;
    private RecordCollection priors = null;
    private RecordCollection posts = null;
    
    public RecordView(){
        collection = new RecordCollection();
        priors = new RecordCollection();
        posts = new RecordCollection();
    }
    
    public RecordView (int capacity) {
        collection = new RecordCollection(capacity);
        priors = new RecordCollection(capacity);
        posts = new RecordCollection(capacity);
    }
    
    public RecordView (final RecordView view) {
        if (view == null) {
            collection = new RecordCollection();
            priors = new RecordCollection();
            posts = new RecordCollection();
        } else {
            collection = new RecordCollection(view.collection);
            priors = new RecordCollection(view.priors);
            posts = new RecordCollection(view.posts);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public RecordView clone () {
        return new RecordView (this);
    }
    
    /**
     * Get a copy of the instrument.
     * 
     * @return RecordView
     */
    
    public RecordView copy () {
        return new RecordView(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<Record> iterator() {
        return new RecordWindowIterator(this.collection);
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<Record> iterator(final int begin, final int end) {
        return new RecordWindowIterator(this.collection, begin, end);
    }
    
    /** {@inheritDoc} */
    @Override
    public Iterator<Record> reverseIterator(final int begin, final int end) {
        return new RecordWindowReverseIterator(this.collection, begin, end);
    }
    
    /** {@inheritDoc} */
    @Override
    public Record peek() {
        return collection.peek();
    }

    /** {@inheritDoc} */
    @Override
    public Record peek(final int n) {
        if (n < 0) {
            return priors.peekLast(n * -1 - 1);
        }
        
        final int size = collection.size();
        return n < size ? collection.get(n) : posts.get(n - size);
    }

    /** {@inheritDoc} */
    @Override
    public Record peekLast() {
        return collection.peekLast();
    }

    /** {@inheritDoc} */
    @Override
    public Record peekLast(final int n) {
        if (n < 0) {
            return posts.peekLast(n * -1 - 1);
        }
        
        final int size = collection.size();
        return n < size ? collection.peekLast(n) : priors.peekLast(n - size);
    }

    /**
     * Remove the first record in the instrument.
     * 
     * @return Record
     */
    public Record poll() {
        return collection.poll();
    }

    /**
     * Remove the first record in the priors.
     * 
     * @return Record
     */
    public Record pollPriors() {
        return priors.poll();
    }

    /**
     * Remove the first record in the posts.
     * 
     * @return Record
     */
    public Record pollPosts() {
        return posts.poll();
    }

    /**
     * Remove the last record in the instrument.
     * 
     * @return Record
     */
    public Record pollLast() {
        return collection.pollLast();
    }

    /**
     * Remove the last record in the priors.
     * 
     * @return Record
     */
    public Record pollLastPriors() {
        return priors.pollLast();
    }

    /**
     * Remove the last record in the posts.
     * 
     * @return Record
     */
    public Record pollLastPosts() {
        return posts.pollLast();
    }
    
    /** {@inheritDoc} */
    @Override
    public Record get (final int position) {
        if (position < 0) {
            return priors.peekLast(position * -1 - 1);
        }
        
        final int size = collection.size();
        return position < size ? collection.get(position) : posts.get(position - size);
    }
    
    /** {@inheritDoc} */
    @Override
    public int size () {
        return collection.size();
    }
    
    public int sizePriors () {
        return priors.size();
    }
    
    public int sizePosts () {
        return posts.size();
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }
    
    /** {@inheritDoc} */
    @Override
    public void clear() {
        collection.clear();
    }
    
    public void reset() {
        priors.addAll(collection);
        collection.clear();
    }
    
    public void clearPriors() {
        priors.clear();
    }
    
    public void clearPosts() {
        posts.clear();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return collection.hashCode();
    }
    
    /**
     * Get all records.
     * 
     * @return List of Records
     */
    public List<Record> getAll() {
        final ArrayList<Record> all = new ArrayList<>(collection.size() + priors.size() + posts.size());
        all.addAll(priors.getAll());
        all.addAll(collection.getAll());
        all.addAll(posts.getAll());
        return all;
    }
    
    public RecordCollection getPriors() {
        return priors;
    }
    
    public RecordCollection getPosts() {
        return posts;
    }
    
    /**
     * Add a record.
     * 
     * @param record the record
     * @return RecordView
     */
    public RecordView add(final Record record) {
        collection.add(record);
        return this;
    }
    
    /**
     * Add a record to priors.
     * 
     * @param record the record
     * @return RecordView
     */
    public RecordView addPriors(final Record record) {
        priors.add(record);
        return this;
    }
    
    /**
     * Add a record to posts.
     * 
     * @param record the record
     * @return RecordView
     */
    public RecordView addPosts(final Record record) {
        posts.add(record);
        return this;
    }
    
    /**
     * Remove a record at the specified position.
     * 
     * @param position the position
     * @return Record
     */
    public Record remove (final int position) {
        return collection.remove(position);
    }
    
    /**
     * Remove a record at the specified position in priors.
     * 
     * @param position the position
     * @return Record
     */
    public Record removePriors (final int position) {
        return priors.remove(position);
    }
    
    /**
     * Remove a record at the specified position in posts.
     * 
     * @param position the position
     * @return Record
     */
    public Record removePosts (final int position) {
        return posts.remove(position);
    }
 
    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RecordView)) {
            return false;
        }
        
        final RecordView view = (RecordView) obj;
        return this.collection.compareTo(view.collection) == 0;
    }
    
    public RecordCollection toCollection() {
        final List<Record> newList = new ArrayList<>(collection.size() + priors.size() + posts.size());
        newList.addAll(priors.getAll());
        newList.addAll(collection.getAll());
        newList.addAll(posts.getAll());
        return new RecordCollection(newList);
    }
}
