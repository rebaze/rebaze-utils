/*
 * Copyright (c) 2012-2013 rebaze GmbH
 * All rights reserved. 
 * 
 * This library and the accompanying materials are made available under the terms of the Apache License Version 2.0,
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.
 *
 */
package org.auxis.commons.tree;

import static org.auxis.commons.tree.Selector.selector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.auxis.commons.tree.internal.InMemoryTreeBuilderImpl;
import org.auxis.commons.tree.util.TreeTools;
import org.junit.Test;

public class SimpleHashTest
{
    private TreeTools m_tools = TreeTools.treeTools();

    @Test
    public void emptyCollector() throws Exception
    {
        TreeBuilder root = m_tools.createTreeBuilder().selector( selector( "Root" ) );
        assertEquals(InMemoryTreeBuilderImpl.FIXED_EMPTY, root.seal().fingerprint() );

        TreeBuilder root2 = m_tools.createTreeBuilder().selector( selector( "RootOther" ) );
        assertEquals( root.seal().fingerprint(), root2.seal().fingerprint() );
    }

    @Test
    public void singleElements() throws Exception
    {
        String[] elements = new String[] { "element1", "element2" };
        TreeBuilder root = m_tools.createTreeBuilder().selector( selector( "Root" ) );
        root.add( elements[0].getBytes() );
        root.add( elements[1].getBytes() );
        assertEquals( "2a190d88c7a164f242ea707acf5d57bc990f0ce1", root.seal().fingerprint() );

        String[] elementsOther = new String[] { "foo", "element2" };
        TreeBuilder root2 = m_tools.createTreeBuilder().selector( selector( "Root" ) );
        root2.add( elementsOther[0].getBytes() );
        root2.add( elementsOther[1].getBytes() );
        assertEquals( "0dc0638a504e1b0415a37340bf57d14b75b14308", root2.seal().fingerprint() );
    }

    @Test
    public void simpleTreeTest() throws Exception
    {
        TreeBuilder root = m_tools.createTreeBuilder().selector( selector( "Root1" ) );
        TreeBuilder sub1 = root.branch( selector( "child1" ) );
        sub1.add( "One".getBytes() );
        sub1.add( "Two".getBytes() );

        TreeBuilder sub2 = root.branch( selector( "child2" ) );
        sub2.add( "Three".getBytes() );
        sub2.add( "Four".getBytes() );
        Tree tree1 = root.seal();

        TreeBuilder root2 = m_tools.createTreeBuilder().selector( selector( "Root2" ) );
        TreeBuilder sub3 = root2.branch( selector( "child3" ) );
        sub3.add( "Different".getBytes() );
        sub3.add( "Two".getBytes() );

        TreeBuilder sub4 = root2.branch( selector( "child4" ) );
        sub4.add( "Three".getBytes() );
        sub4.add( "Four".getBytes() );
        Tree tree2 = root2.seal();

        assertNotEquals( "roots must be different", tree1.fingerprint(), tree2.fingerprint() );
        assertNotEquals( "first childs must be different", tree1.branches()[0].fingerprint(), tree2.branches()[0].fingerprint() );

    }

    @Test
    public void testReuseCollectors()
    {
        TreeBuilder sn1 = m_tools.createTreeBuilder();
        sn1.branch( selector( "p1" ) ).add( "one".getBytes() );
        sn1.branch( selector( "p1" ) ).add( "two".getBytes() );

        TreeBuilder sn2 = m_tools.createTreeBuilder();
        sn2.branch( selector( "p1" ) ).add( "one".getBytes() ).add( "two".getBytes() );

        assertEquals( "Should no elements", sn1.seal(),sn2.seal() );
    }
}
