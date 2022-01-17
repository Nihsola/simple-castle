package castle.core.game.component

import castle.core.common.path.Area
import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.ai.pfa.GraphPath

class PathComponent(val path: List<String>) : Component {
    var graphPath: GraphPath<Area> = DefaultGraphPath()
    var nextPosition: Int = 0

    companion object {
        val mapper: ComponentMapper<PathComponent> = ComponentMapper.getFor(PathComponent::class.java)
    }
}