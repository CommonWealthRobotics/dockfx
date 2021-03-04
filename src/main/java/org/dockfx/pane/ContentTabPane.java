package org.dockfx.pane;

import javafx.scene.control.Tooltip;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Skin;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import org.dockfx.DockNode;
import org.dockfx.DockPos;
import org.dockfx.pane.skin.ContentTabPaneSkin;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
/**
 * ContentTabPane holds multiple tabs
 *
 * @author HongKee Moon
 */
public class ContentTabPane extends TabPane implements ContentPane
{

  ContentPane parent;

  public ContentTabPane()
  {
	    getTabs().addListener((ListChangeListener<? super Tab>)  observable -> {
	      updateTabWidth();
	    });
	    widthProperty().addListener((obs, oldVal, newVal) -> {
	      updateTabWidth();
	    });
  }
  private void updateTabWidth(){
	    if(getTabs().size()<1)
	      return;
	    int sizeOffsetToRemoveTheCarrot = 105;
		double w = (getWidth()-sizeOffsetToRemoveTheCarrot)/((double)getTabs().size());
	    setTabMaxWidth(w);
	    setTabMinWidth(w);
  }

  /** {@inheritDoc} */
  @Override
  protected Skin<?> createDefaultSkin()
  {
    return new ContentTabPaneSkin(this);
  }

  public Type getType()
  {
    return Type.TabPane;
  }

  public void setContentParent(ContentPane pane)
  {
    parent = pane;
  }

  public ContentPane getContentParent()
  {
    return parent;
  }

  public ContentPane getSiblingParent(Stack<Parent> stack,
                                      Node sibling)
  {
    ContentPane pane = null;

    while (!stack.isEmpty())
    {
      Parent parent = stack.pop();

      List<Node> children = parent.getChildrenUnmodifiable();

      if (parent instanceof ContentPane)
      {
        children = ((ContentPane) parent).getChildrenList();
      }

      for (int i = 0; i < children.size(); i++)
      {
        if (children.get(i) == sibling)
        {
          pane = (ContentPane) parent;
        }
        else if (children.get(i) instanceof Parent)
        {
          stack.push((Parent) children.get(i));
        }
      }
    }
    return pane;
  }

  public boolean removeNode(Stack<Parent> stack, Node node)
  {
    List<Node> children = getChildrenList();

    for (int i = 0; i < children.size(); i++)
    {
      if (children.get(i) == node)
      {
        getTabs().remove(i);
        return true;
      }
    }

    return false;
  }

  public void set(int idx, Node node)
  {
	DockNodeTab t = makeDNT(node);
	
	getTabs().set(idx, t);
    getSelectionModel().select(idx);
  }

  public void set(Node sibling, Node node)
  {
    set(getChildrenList().indexOf(sibling), node);
  }

  public List<Node> getChildrenList()
  {
    return getTabs().stream()
                    .map(i -> i.getContent())
                    .collect(Collectors.toList());
  }

  public void addNode(Node root,
                      Node sibling,
                      Node node,
                      DockPos dockPos)
  {
    DockNodeTab t = makeDNT(node);
    addDockNodeTab(t);
  }
  private DockNodeTab makeDNT(Node node) {
	DockNode newNode = (DockNode) node;
    DockNodeTab t = new DockNodeTab(newNode);
    newNode.setFocusRequestListener(() -> {
		Platform.runLater(() -> {
			javafx.scene.control.SingleSelectionModel<Tab> selectionModel = getSelectionModel();
			selectionModel.select(t);
		});
	});
	return t;
  }

	public void addDockNodeTab(DockNodeTab dockNodeTab) {
		dockNodeTab.dockNode.setFocusRequestListener(() -> {
			Platform.runLater(() -> {
				javafx.scene.control.SingleSelectionModel<Tab> selectionModel = getSelectionModel();
				selectionModel.select(dockNodeTab);
			});
		});
		dockNodeTab.setTooltip(new Tooltip(dockNodeTab.getTitle()));
		getTabs().add(dockNodeTab);
		getSelectionModel().select(dockNodeTab);

	}

  @Override
  protected double computeMaxWidth(double height)
  {
    return getTabs().stream()
                    .map(i -> i.getContent().maxWidth(height))
                    .min(Comparator.naturalOrder())
                    .get();
  }
}
