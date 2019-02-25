package com.badoo.ribs.example.rib.hello_world.builder

import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorld.Input
import com.badoo.ribs.example.rib.hello_world.HelloWorld.Output
import com.badoo.ribs.example.rib.hello_world.HelloWorldInteractor
import com.badoo.ribs.example.rib.hello_world.HelloWorldRouter
import com.badoo.ribs.example.rib.hello_world.HelloWorldView
import com.badoo.ribs.example.rib.hello_world.feature.HelloWorldFeature
import com.uber.rib.core.Node
import com.uber.rib.core.ViewFactory
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object HelloWorldModule {

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun router(
        // pass component to child rib builders, or remove if there are none
        component: HelloWorldComponent
    ): HelloWorldRouter =
        HelloWorldRouter()

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun feature(): HelloWorldFeature =
        HelloWorldFeature()

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun interactor(
        router: HelloWorldRouter,
        input: ObservableSource<Input>,
        output: Consumer<Output>,
        feature: HelloWorldFeature
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            router = router,
            input = input,
            output = output,
            feature = feature
        )

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun node(
        viewFactory: ViewFactory<HelloWorldView>,
        router: HelloWorldRouter,
        interactor: HelloWorldInteractor
    ) : Node<HelloWorldView> = Node(
        forClass = HelloWorld::class.java,
        viewFactory = viewFactory,
        router = router,
        interactor = interactor
    )
}
