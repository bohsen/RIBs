package com.badoo.ribs.example.rib.hello_world.builder

import com.badoo.ribs.example.rib.hello_world.HelloWorld
import com.badoo.ribs.example.rib.hello_world.HelloWorldView
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node

class HelloWorldBuilder(dependency: HelloWorld.Dependency) :
    Builder<HelloWorld.Dependency>(dependency) {

    fun build(): Node<HelloWorldView> {
        val customisation = dependency.ribCustomisation().get(HelloWorld.Customisation::class) ?: HelloWorld.Customisation()
        val component = DaggerHelloWorldComponent.factory()
            .create(dependency, customisation)

        return component.node()
    }
}
