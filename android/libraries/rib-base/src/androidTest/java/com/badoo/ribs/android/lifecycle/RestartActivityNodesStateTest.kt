package com.badoo.ribs.android.lifecycle

import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.test.util.restartActivitySync
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode1AsDialog
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode1AsDialogAndOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode1AsOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode2
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode2AsDialog
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode2AsDialogAndOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode2AsOverlay
import com.badoo.ribs.test.util.runOnMainSync
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class RestartActivityNodesStateTest(private val test: Pair<When, ExpectedState>) : BaseNodesTest() {

    companion object {
        @JvmStatic
        @Suppress("LongMethod")
        @Parameters(name = "{0}")
        fun data() = listOf(
            When(pushConfiguration1 = AttachNode1, pushConfiguration2 = AttachNode2)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(pushConfiguration1 = AttachNode1, pushConfiguration2 = AttachNode2AsOverlay)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = true), node2 = NodeState(attached = true, viewAttached = true)),

            When(pushConfiguration1 = AttachNode1, pushConfiguration2 = AttachNode2AsDialog)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false)),

            When(pushConfiguration1 = AttachNode1, pushConfiguration2 = AttachNode2AsDialogAndOverlay)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = true), node2 = NodeState(attached = true, viewAttached = false)),


            When(pushConfiguration1 = AttachNode1AsOverlay, pushConfiguration2 = AttachNode2)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(pushConfiguration1 = AttachNode1AsOverlay, pushConfiguration2 = AttachNode2AsOverlay)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = true), node2 = NodeState(attached = true, viewAttached = true)),

            When(pushConfiguration1 = AttachNode1AsOverlay, pushConfiguration2 = AttachNode2AsDialog)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false)),

            When(pushConfiguration1 = AttachNode1AsOverlay, pushConfiguration2 = AttachNode2AsDialogAndOverlay)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = true), node2 = NodeState(attached = true, viewAttached = false)),


            When(pushConfiguration1 = AttachNode1AsDialog, pushConfiguration2 = AttachNode2)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(pushConfiguration1 = AttachNode1AsDialog, pushConfiguration2 = AttachNode2AsOverlay)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(pushConfiguration1 = AttachNode1AsDialog, pushConfiguration2 = AttachNode2AsDialog)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false)),

            When(pushConfiguration1 = AttachNode1AsDialog, pushConfiguration2 = AttachNode2AsDialogAndOverlay)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false)),


            When(pushConfiguration1 = AttachNode1AsDialogAndOverlay, pushConfiguration2 = AttachNode2)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(pushConfiguration1 = AttachNode1AsDialogAndOverlay, pushConfiguration2 = AttachNode2AsOverlay)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(pushConfiguration1 = AttachNode1AsDialogAndOverlay, pushConfiguration2 = AttachNode2AsDialog)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false)),

            When(pushConfiguration1 = AttachNode1AsDialogAndOverlay, pushConfiguration2 = AttachNode2AsDialogAndOverlay)
                to ExpectedState(node1 = NodeState(attached = false, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false))
        )
    }

    @get:Rule
    val ribsRule = RibsRule { rootProvider.invoke() }

    @Test
    fun pushTwoConfigurationsAndRestartActivity() {
        runOnMainSync {
            router.push(test.first.pushConfiguration1)
            router.push(test.first.pushConfiguration2)
        }

        ribsRule.restartActivitySync()

        makeAssertions(test.second)
    }

    class When(
        val pushConfiguration1: Configuration,
        val pushConfiguration2: Configuration
    ) {
        override fun toString() = "push 1 = ${pushConfiguration1::class.java.simpleName} " +
            "push 2 = ${pushConfiguration2::class.java.simpleName}]"
    }
}
