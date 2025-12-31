package me.bobulo.mine.pdam.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.*
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.events.UIClickEvent
import java.awt.Color

data class PacketNote(
    val date: String,
    val packetName: String,
    val packetData: String,
    val tags: List<String>
)

class PacketGui : WindowScreen(ElementaVersion.V10) {
    private val packetNotes = mutableListOf<PacketNote>()
    private val scrollComponent = ScrollComponent("Empty").constrain {
        x = CenterConstraint()
        y = 60.pixels()
        width = 90.percent()
        height = 80.percent()
    } childOf window

    private val searchBox = UIRoundedRectangle(5f).constrain {
        x = 10.pixels()
        y = 10.pixels()
        width = 60.percent()
        height = 20.pixels()
        color = Color(50, 50, 50, 200).toConstraint()
    } childOf window

    private val searchBar = UITextInput("Pesquisar...").constrain {
        x = 2.pixels()
        y = 2.pixels()

        width = RelativeConstraint(1f) - 6.pixels()
    } childOf searchBox

    private val filterInput = UITextInput("Filtrar por tags...").constrain {
        x = 10.pixels(alignOpposite = true)
        y = 10.pixels()
        width = 30.percent()
        height = 20.pixels()
    } childOf window

    private val inspector = Inspector(window).constrain {
        x = 10.pixels(true)
        y = 10.pixels(true)
    } childOf window

    init {
        searchBox.onMouseClick { searchBar.grabWindowFocus() }

        searchBar.onKeyType { _, _ ->
            refreshTable()
        }

        filterInput.onKeyType { _, _ ->
            refreshTable()
        }

        scrollComponent.setVerticalScrollBarComponent(UIBlock().constrain {
            x = scrollComponent.getRight().pixels() + 5.pixels() // X máximo + espaçamento
            y = 100.pixels()
            width = 1.percent()
            height = 1.percent()
            color = Color(100, 100, 100, 200).toConstraint()
        } childOf window)

    }

    fun addNote(packetNote: PacketNote) {
        packetNotes.add(packetNote)
//        refreshTable()
    }

    fun refreshTable() {
        scrollComponent.clearChildren()

        val searchText = searchBar.getText().lowercase()
        val filterText = filterInput.getText().lowercase()

        packetNotes.filter { note ->
            val matchesSearch = searchText.isEmpty() ||
                    note.packetData.lowercase().contains(searchText) ||
                    note.date.contains(searchText)
            val matchesFilter = filterText.isEmpty() ||
                    note.tags.any { it.lowercase().contains(filterText) }
            matchesSearch && matchesFilter
        }.forEachIndexed { index, note ->
            createTableRow(note, index) childOf scrollComponent
        }
    }

    private fun createTableRow(packetNote: PacketNote, index: Int): UIComponent {
        var isExpanded = false

        val childBasedMaxSizeConstraint = ChildBasedSizeConstraint()
        val rowContainer = UIContainer().constrain {
            y = SiblingConstraint(5f)
            width = 100.percent()
            height = childBasedMaxSizeConstraint + 10.pixels()
        }

        val rowBackground = UIRoundedRectangle(
            radius = 5f
        ).constrain {
            width = 100.percent()
            height = 100.percent()
            color = if (index % 2 == 0) Color(50, 50, 50, 150).toConstraint() else Color(70, 70, 70, 150).toConstraint()
        } childOf rowContainer

        // dateLabel
//        UIText(note.date).constrain {
//            x = 5.pixels()
//            y = 5.pixels()
////            width = 35.pixels()
//            width = 5.percent()
//        } childOf rowContainer

        val dateContainer = UIContainer().constrain {
            x = 5.percent()
            y = 5.pixels()
            width = 55.percent()
        } childOf rowContainer

        val dateLabel = UIText(packetNote.date).constrain {
            width = 30.pixels()
        } childOf dateContainer

        // categoryLabel
        UIText(packetNote.packetName).constrain {
            x = 20.percent()
            y = 5.pixels()
            width = 55.pixels()
        } childOf rowContainer

        val textContainer = UIContainer().constrain {
            x = 40.percent()
            y = 5.pixels()
            width = 55.percent()
            height = ChildBasedSizeConstraint()
        } childOf rowContainer
        childBasedMaxSizeConstraint.constrainTo = textContainer

        val textLabel = UIWrappedText("", centered = false).constrain {
            width = 100.percent()
        } childOf textContainer


        // Função para atualizar o texto
        fun updateText() {
            if (isExpanded) {
                textLabel.setText(packetNote.packetData)
            } else {
                val truncated = if (packetNote.packetData.length > 50)
                    packetNote.packetData.take(50) + "..."
                else packetNote.packetData
                textLabel.setText(truncated)
            }
        }

        // Inicializar com texto truncado
        updateText()

        rowContainer.onMouseClick {
            isExpanded = !isExpanded
            updateText()
        }

        return rowContainer
    }


}