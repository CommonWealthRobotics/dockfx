package org.dockfx.pane;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

import org.dockfx.DockNode;
import org.dockfx.DockTitleBar;

/**
 * DockNodeTab class holds Tab for ContentTabPane
 *
 * @author HongKee Moon
 */
public class DockNodeTab extends Tab
{

  final public DockNode dockNode;

  final private SimpleStringProperty title;
  DockTitleBar dockTitleBar;
  public DockNodeTab(DockNode node)
  {
    this.dockNode = node;
    setClosable(false);

    title = new SimpleStringProperty("");
    title.bind(dockNode.titleProperty());

    dockTitleBar = dockNode.getDockTitleBar();
	setGraphic(dockTitleBar);
    setContent(dockNode);
    dockNode.tabbedProperty().set(true);
    dockNode.setNodeTab(this);
  }

  public String getTitle()
  {
    return title.getValue();
  }

  public SimpleStringProperty titleProperty()
  {
    return title;
  }

  public void select()
  {
    getTabPane().getSelectionModel().select(this);
  }
  public void setWidthOfGraphic(double w) {
	  dockTitleBar.setPrefWidth(w);
  }
}
