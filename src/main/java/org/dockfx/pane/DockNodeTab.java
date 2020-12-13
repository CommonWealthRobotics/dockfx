package org.dockfx.pane;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Tab;

import org.dockfx.DockNode;

/**
 * DockNodeTab class holds Tab for ContentTabPane
 *
 * @author HongKee Moon
 */
public class DockNodeTab extends Tab
{

  final private DockNode dockNode;

  final private StringProperty title;

  public DockNodeTab(DockNode node)
  {
    this.dockNode = node;
    setClosable(false);

    title = dockNode.titleProperty();
    //title.bind(dockNode.titleProperty());

    setGraphic(dockNode.getDockTitleBar());
    setContent(dockNode);
    dockNode.tabbedProperty().set(true);
    dockNode.setNodeTab(this);
  }

  public String getTitle()
  {
    //new RuntimeException("Getting name "+title.getValue()).printStackTrace();
    return title.getValue();
  }

  public StringProperty titleProperty()
  {
    //new RuntimeException("Getting name prop"+title.getValue()).printStackTrace();
    return title;
  }

  public void select()
  {
    getTabPane().getSelectionModel().select(this);
  }
}
