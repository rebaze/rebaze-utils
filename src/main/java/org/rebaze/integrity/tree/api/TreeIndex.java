/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.rebaze.integrity.tree.api;

import org.rebaze.integrity.tree.internal.AbstractDelegateTree;

import java.util.HashMap;
import java.util.Map;

/**
 * Index implementation that augments a given tree with some extra index accessors like find by
 * selector and tag.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 */
public class TreeIndex extends AbstractDelegateTree
{
    final private Map<Selector, TreeIndex> m_selectors = new HashMap<>();
    final private Map<String, TreeIndex> m_firstResponderTree = new HashMap<>();
    final private TreeIndex[] m_sub;

    public TreeIndex( Tree tree )
    {
        super( tree );
        guardIlegalWrappedTreeIndex( tree );
        m_sub = createDeepIndex( tree );
        indexResponder(this);
    }

    private void indexResponder( TreeIndex tree )
    {
        m_firstResponderTree.putIfAbsent( tree.value().hash(), tree );
        for (TreeIndex t : tree.branches()) {
            indexResponder( t );
        }
    }

    private TreeIndex[] createDeepIndex( Tree tree )
    {
        for ( Tree h : tree.branches() )
        {
            m_selectors.put( h.selector(), new TreeIndex( h ) );
        }
        return m_selectors.values().toArray( new TreeIndex[0] );
    }

    public TreeIndex select( Selector selector )
    {
        return m_selectors.get( selector );
    }

    public TreeIndex select( Selector... selectors )
    {
        TreeIndex r = this;
        for ( Selector s : selectors )
        {
            r = r.select( s );
        }
        return r;
    }

    @Override
    public TreeIndex[] branches()
    {
        return m_sub;
    }

    public boolean contains( Tree tree) {
        if (tree != null)
        {
            return m_firstResponderTree.containsKey( tree.value().hash() );
        }else {
            return false;
        }
    }

    public String toString()
    {
        return super.toString() + " Idx: " + m_selectors.size();
    }

}
