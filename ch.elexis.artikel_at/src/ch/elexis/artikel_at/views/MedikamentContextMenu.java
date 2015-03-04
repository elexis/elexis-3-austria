package ch.elexis.artikel_at.views;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

import ch.elexis.artikel_at.data.Medikament;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.core.ui.icons.Images;
import ch.elexis.core.ui.util.viewers.CommonViewer;
import ch.elexis.core.ui.views.artikel.ArtikelContextMenu.ArtikelDetailDisplay;
import ch.elexis.core.ui.views.artikel.Messages;
import ch.elexis.data.Artikel;


public class MedikamentContextMenu {
	private IAction propertiesAction;
	CommonViewer cv;
	ArtikelDetailDisplay add;
	ArtikelMenuListener menuListener = new ArtikelMenuListener();
	MenuManager menu;
	ArrayList<IAction> actions = new ArrayList<IAction>();
	
	public MedikamentContextMenu(final Medikament template, final CommonViewer cv,
		final ArtikelDetailDisplay add){
		this(template, cv);
		this.add = add;
	}
	
	public MedikamentContextMenu(final Medikament template, final CommonViewer cv){
		this.cv = cv;
		makeActions(template);
		actions.add(propertiesAction);
		menu = new MenuManager();
		menu.addMenuListener(menuListener);
		cv.setContextMenu(menu);
	}
	
	public void addAction(final IAction ac){
		actions.add(ac);
	}
	
	public void removeAction(final IAction ac){
		actions.remove(ac);
	}
	
	public interface MedikamentDetailDisplay {
		public boolean show(Medikament art);
	}
	
	class ArtikelMenuListener implements IMenuListener {
		public void menuAboutToShow(final IMenuManager manager){
			menu.removeAll();
			for (IAction ac : actions) {
				if (ac == null) {
					menu.add(new Separator());
				} else {
					menu.add(ac);
				}
			}
		}
	}
	
	private void makeActions(final Artikel art){
		propertiesAction = new Action(Messages.ArtikelContextMenu_propertiesAction) {
			{
				setImageDescriptor(Images.IMG_EDIT.getImageDescriptor());
				setToolTipText(Messages.ArtikelContextMenu_propertiesTooltip);
			}
			
			@Override
			public void run(){
				Medikament n = (Medikament) ElexisEventDispatcher.getSelected(art.getClass());
				if (add == null) {
					new MedikamentDetailDialog(cv.getViewerWidget().getControl().getShell(), n)
						.open();
				} else {
					add.show(n);
				}
			}
		};
	}
	
}
