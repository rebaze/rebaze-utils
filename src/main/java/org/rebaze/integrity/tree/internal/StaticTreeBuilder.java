/*
 * Copyright 2014 rebaze GmbH. All Rights Reserved.
 */
package org.rebaze.integrity.tree.internal;

import org.rebaze.integrity.tree.api.Selector;
import org.rebaze.integrity.tree.api.Tree;
import org.rebaze.integrity.tree.api.TreeBuilder;
import org.rebaze.integrity.tree.api.Tag;
import org.rebaze.integrity.tree.api.TreeSession;

/**
 * Augments a given tree as {@link TreeBuilder}. Used to incorporate existing {@link Tree}s in Tree
 * construction.
 *
 * @author Toni Menzel <toni.menzel@rebaze.com>
 */
public class StaticTreeBuilder implements TreeBuilder
{
    private Tree m_tree;
    private final TreeSession m_session;

    public StaticTreeBuilder(Tree encapsulatedTree, TreeSession treeSession )
    {
        m_tree = encapsulatedTree;
        m_session = treeSession;
    }

    @Override
    public TreeBuilder add( byte[] bytes )
    {
        throw new UnsupportedOperationException( "Static TreeBuilder does not allow modification." );
    }

    @Override
    public TreeBuilder selector( Selector selector )
    {
        throw new UnsupportedOperationException( "Static TreeBuilder does not allow modification." );
    }

    @Override
    public TreeBuilder branch( Selector selector )
    {
        throw new UnsupportedOperationException( "Static TreeBuilder does not allow modification." );
    }

    @Override
    public TreeBuilder branch( Tree subtree )
    {
        throw new UnsupportedOperationException( "Static TreeBuilder does not allow modification." );
    }

    @Override
    public TreeBuilder tag( Tag tag )
    {
        // TODO: Use compound tag!
        m_tree = m_session.createTree(m_tree.selector(),m_tree.value(),m_tree.branches(), tag);
        return this;
    }

    @Override
    public Tree seal()
    {
        return m_tree;
    }
}
