/*******************************************************************************
 * Copyright (c) 2015 MEDEVIT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 *******************************************************************************/
package at.medevit.elexis.befuem.ui.views;

import java.util.List;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.contextservice.networkparticipant.NetworkParticipant;
import at.medevit.elexis.befuem.netservice.INetClient;
import at.medevit.elexis.befuem.netservice.NetClientPreferenceDescritpion;
import at.medevit.elexis.befuem.ui.Activator;
import at.medevit.elexis.befuem.ui.FindingComparator;
import at.medevit.elexis.befuem.ui.Messages;
import at.medevit.elexis.befuem.ui.SelectionProviderIntermediate;
import at.medevit.elexis.befuem.ui.commands.OpenClientFinding;
import at.medevit.elexis.befuem.ui.commands.OpenClientImport;
import at.medevit.elexis.befuem.ui.filters.FindingNameSenderFilter;
import at.medevit.elexis.befuem.ui.filters.FindingNameSenderReceiverFilter;
import at.medevit.elexis.befuem.ui.filters.NetworkParticipantFilter;
import at.medevit.elexis.befuem.ui.labelprovider.FindingPatientLabelProvider;
import at.medevit.elexis.befuem.ui.labelprovider.FindingReceptionLabelProvider;
import at.medevit.elexis.befuem.ui.labelprovider.FindingSizeLabelProvider;
import at.medevit.elexis.befuem.ui.labelprovider.FindingTypLabelProvider;
import at.medevit.elexis.befuem.ui.model.ArchiveFindingsModelProvider;
import at.medevit.elexis.befuem.ui.model.InboxFindingsModelProvider;
import at.medevit.elexis.befuem.ui.model.NetworkParticipantModelProvider;
import at.medevit.elexis.befuem.ui.model.PreferenceConstants;
import at.medevit.elexis.befuem.ui.netserviceconsumer.NetServiceConsumer;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.icons.Images;
import ch.elexis.data.Kontakt;

import com.swtdesigner.ResourceManager;

public class BefuemView extends ViewPart {
	public static final String ID = "at.medevit.elexis.befuem.ui.befuemView"; //$NON-NLS-1$
	
	private static final String VIEWER_ID = "VIEWER_ID"; //$NON-NLS-1$
	private static final int VIEWER_ID_INBOX = 1;
	private static final int VIEWER_ID_OUTBOX = 2;
	private static final int VIEWER_ID_ARCHIVE = 3;
	private static final int VIEWER_ID_ADDRESSBOOK = 4;
	
	private TableViewer addressbookViewer;
	private Composite tableAddressbook;
	
	private TableViewer inboxViewer;	
	private Composite tableInbox;
//	private TableViewer outboxViewer;	
//	private Table tableOutbox;
	private TableViewer archiveViewer;	
	private Composite tableArchive;
	
	private FindingComparator comparator;
	private FindingNameSenderFilter filterNameSender;
	private FindingNameSenderReceiverFilter filterNameSenderReceiver;
	private NetworkParticipantFilter filterParticipant;

	private CTabFolder tabFolder;
	private SelectionProviderIntermediate intermediatSelection;
	
	private Display display;
	
	public BefuemView() {
		display = Display.getCurrent();
	}

	/**
	 * Trigger update of the Adressbook Viewer.
	 * Can be run from non UI Thread.
	 */
	public void updateAdressbookViewer() {
		NetworkParticipantModelProvider.getInstance().updateModel();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				addressbookViewer.refresh();	
			}
		});
	}
	
	/**
	 * Trigger update of the Inbox Viewer.
	 * Can be run from non UI Thread.
	 */
	public void updateInboxViewer(boolean refreshAll) {
		if(refreshAll)
			InboxFindingsModelProvider.getInstance().updateModel(new JobFinishedListener());
		else
			InboxFindingsModelProvider.getInstance().refreshModel(new JobFinishedListener());
	}
	
	/**
	 * Trigger update of the Archive Viewer.
	 * Can be run from non UI Thread.
	 */
	public void updateArchiveViewer(boolean refreshAll) {
		if(refreshAll)
			ArchiveFindingsModelProvider.getInstance().updateModel(new JobFinishedListener());
		else
			ArchiveFindingsModelProvider.getInstance().refreshModel(new JobFinishedListener());
	}
	
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		intermediatSelection = new SelectionProviderIntermediate();
		// initialize the NetClient with the persistent preferences
		INetClient impl = NetServiceConsumer.getClientImplementation(CoreHub.globalCfg.get(PreferenceConstants.AT_MEDEVIT_ELEXIS_BEFUEM_SELECTED_NET, "GNV")); //$NON-NLS-1$
		NetClientPreferenceDescritpion pref = impl.getPreferenceDescription();
		for(int index = 0; index < pref.size(); index++) {
			impl.setPreferenceValue(pref.getFieldConstantAt(index), CoreHub.globalCfg.get(pref.getFieldConstantAt(index), "")); //$NON-NLS-1$
		}
		// register a listener to update the views content if preferences change
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.addPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty() == PreferenceConstants.AT_MEDEVIT_ELEXIS_BEFUEM_SELECTED_NET) {
					addressbookViewer.refresh();
					InboxFindingsModelProvider.getInstance().updateModel();
					inboxViewer.refresh();
				}
				if (event.getProperty() == INetClient.AT_MEDEVIT_ELEXIS_BEFUEM_GNVCLIENT_PROGLOCATION) {
					INetClient impl = NetServiceConsumer.getClientImplementation("GNV"); //$NON-NLS-1$
					impl.setPreferenceValue(event.getProperty(), (String)event.getNewValue());
					InboxFindingsModelProvider.getInstance().updateModel();
					inboxViewer.refresh();
				}
			}
		});
	}

	@Override
	public void createPartControl(Composite parent) {	
		filterNameSender = new FindingNameSenderFilter();
		filterNameSenderReceiver = new FindingNameSenderReceiverFilter();
		filterParticipant = new NetworkParticipantFilter();
		comparator = new FindingComparator();
		
		tabFolder = new CTabFolder(parent, SWT.BORDER);
		tabFolder.setSimple(false);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem inboxTab = new CTabItem(tabFolder, SWT.NONE);
		inboxTab.setData(VIEWER_ID, VIEWER_ID_INBOX);
		inboxTab.setImage(ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/inbox_into.png")); //$NON-NLS-1$ //$NON-NLS-2$
		inboxTab.setText(Messages.BefuemView_InboxTabText);
		
		inboxTab.setControl(createInboxComposite(tabFolder));
		inboxViewer.addFilter(filterNameSender);
		inboxViewer.setComparator(comparator);
		
//		CTabItem outboxTab = new CTabItem(tabFolder, SWT.NONE);
//		outboxTab.setData(VIEWER_ID, VIEWER_ID_OUTBOX);
//		outboxTab.setImage(ResourceManager.getPluginImage("at.medevit.elexis.befuem", "rsc/outbox_out.png"));
//		outboxTab.setText("Outbox");
//		
//		outboxTab.setControl(createOutboxComposite(tabFolder));
//		outboxViewer.addFilter(filterNameSender);
//		outboxViewer.setComparator(comparator);
		
		CTabItem archiveTab = new CTabItem(tabFolder, SWT.NONE);
		archiveTab.setData(VIEWER_ID, VIEWER_ID_ARCHIVE);
		archiveTab.setImage(ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/outbox.png")); //$NON-NLS-1$ //$NON-NLS-2$
		archiveTab.setText(Messages.BefuemView_ArchivTabText);
		
		archiveTab.setControl(createArchiveComposite(tabFolder));
		archiveViewer.addFilter(filterNameSenderReceiver);
		archiveViewer.setComparator(comparator);
		
		CTabItem addressbookTab = new CTabItem(tabFolder, SWT.NONE);
		addressbookTab.setData(VIEWER_ID, VIEWER_ID_ADDRESSBOOK);
		addressbookTab.setImage(Images.IMG_BOOK.getImage());
		addressbookTab.setText(Messages.BefuemView_AddressbookTabText);
		
		addressbookTab.setControl(createAddressbookComposite(tabFolder));
		addressbookViewer.addFilter(filterParticipant);
		
		// create context menus for the viewers
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(inboxViewer.getTable());
		inboxViewer.getTable().setMenu(menu);
		getSite().registerContextMenu(menuManager, inboxViewer);

		menu = menuManager.createContextMenu(archiveViewer.getTable());
		archiveViewer.getTable().setMenu(menu);
		getSite().registerContextMenu(menuManager, archiveViewer);
		
		menu = menuManager.createContextMenu(addressbookViewer.getTable());
		addressbookViewer.getTable().setMenu(menu);
		getSite().registerContextMenu(menuManager, addressbookViewer);

		// install delegation of current SelectionProvider to
		// current selected TableViewer
		getSite().setSelectionProvider(intermediatSelection);
		
		tabFolder.addSelectionListener(new SelectionAdapter() {
			// keep track of our current SelectionProvider ...
			@Override
			public void widgetSelected(SelectionEvent e) {
				CTabItem item = tabFolder.getSelection();
				if(item != null) {
					int viewer_id = (Integer) item.getData(VIEWER_ID);

					if(viewer_id == VIEWER_ID_INBOX)
						intermediatSelection.setSelectionProviderDelegate(inboxViewer);
//					else if(viewer_id == VIEWER_ID_OUTBOX)
//						intermediatSelection.setSelectionProviderDelegate(outboxViewer);
					else if(viewer_id == VIEWER_ID_ARCHIVE)
						intermediatSelection.setSelectionProviderDelegate(archiveViewer);
					else if(viewer_id == VIEWER_ID_ADDRESSBOOK)
						intermediatSelection.setSelectionProviderDelegate(addressbookViewer);
				}
			}
		});
		// initially show inbox ...
		tabFolder.setSelection(0);
		intermediatSelection.setSelectionProviderDelegate(inboxViewer);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	private Composite createInboxComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FormLayout());
		
		createInboxViewer(composite);
		
		Text filterTxt = createTableViewerFilter(composite, inboxViewer);
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(filterTxt, 5);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		fd.bottom = new FormAttachment(100, -5);
		tableInbox.setLayoutData(fd);

		return composite;
	}
	
//	private Composite createOutboxComposite(Composite parent) {
//		Composite composite = new Composite(parent, SWT.NONE);
//		composite.setLayout(new FormLayout());
//
//		createOutboxViewer(composite);
//		
//		Text filter = createTableViewerFilter(composite, outboxViewer);
//		
//		FormData fd = new FormData();
//		fd.top = new FormAttachment(filter, 5);
//		fd.left = new FormAttachment(0, 5);
//		fd.right = new FormAttachment(100, -5);
//		fd.bottom = new FormAttachment(100, -5);
//		tableOutbox.setLayoutData(fd);
//
//		return composite;
//	}
	
	private Composite createArchiveComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FormLayout());

		createArchiveViewer(composite);
		
		Text filter = createTableViewerFilter(composite, archiveViewer);
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(filter, 5);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		fd.bottom = new FormAttachment(100, -5);
		tableArchive.setLayoutData(fd);

		return composite;
	}
	
	private Composite createAddressbookComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FormLayout());

		createAddressbookViewer(composite);
		
		Text filter = createTableViewerFilter(composite, addressbookViewer);
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(filter, 5);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		fd.bottom = new FormAttachment(100, -5);
		tableAddressbook.setLayoutData(fd);

		return composite;
	}	
	
	private Text createTableViewerFilter(Composite parent, TableViewer viewer) {
		Label lblFilter = new Label(parent, SWT.NONE);
		lblFilter.setText(Messages.BefuemView_FilterLabelText);
		
		final Text txtFilter = new Text(parent, SWT.BORDER | SWT.SEARCH);
		txtFilter.setText(""); //$NON-NLS-1$
		txtFilter.addKeyListener(new FilterKeyListener(txtFilter, viewer));
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(0, 5);
		lblFilter.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(lblFilter, 5);
		fd.right = new FormAttachment(100, -5);
		txtFilter.setLayoutData(fd);
		
		return txtFilter;
	}
	
	private TableViewer createArchiveViewer(Composite parent) {
		// create a extra Composite for the table when using TableColumnLayout
		tableArchive = new Composite(parent, SWT.NONE);
		tableArchive.setLayout(new FillLayout());
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableArchive.setLayout(tableColumnLayout);
		
		archiveViewer = new TableViewer(tableArchive, SWT.BORDER | SWT.FULL_SELECTION);
		archiveViewer.setData(VIEWER_ID, VIEWER_ID_ARCHIVE);
		Table table = archiveViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn typViewerColumn = new TableViewerColumn(archiveViewer, SWT.NONE);
		TableColumn column = typViewerColumn.getColumn();
		column.setText(Messages.BefuemView_FindingTypColumnText);
		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_TYP, column, archiveViewer));
		tableColumnLayout.setColumnData(column, new ColumnPixelData(75));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(archiveViewer, SWT.NONE);
		column = tableViewerColumn.getColumn();
		column.setText(Messages.BefuemView_FindingSenderColumnText);
		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_SENDER, column, archiveViewer));
		tableColumnLayout.setColumnData(column, new ColumnWeightData(33, 100, true));
		
		tableViewerColumn = new TableViewerColumn(archiveViewer, SWT.NONE);
		column = tableViewerColumn.getColumn();
		column.setText(Messages.BefuemView_FindingReceiverColumnText);
		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_RECEIVER, column, archiveViewer));
		tableColumnLayout.setColumnData(column, new ColumnWeightData(33, 100, true));
		
		// Label Provider will insert Patient info after finding was visited by update job
		TableViewerColumn nameViewerColumn = new TableViewerColumn(archiveViewer, SWT.NONE);
		column = nameViewerColumn.getColumn();
		column.setText(Messages.BefuemView_FindingPatientColumnText);
//		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_NAME, column, archiveViewer));
		tableColumnLayout.setColumnData(column, new ColumnWeightData(33, 100, true));
		
		TableViewerColumn sizeColumn = new TableViewerColumn(archiveViewer, SWT.NONE);
		column = sizeColumn.getColumn();
		column.setWidth(100);
		column.setText(Messages.BefuemView_FindingSizeColumnText);
		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_SIZE, column, archiveViewer));
		column.setAlignment(SWT.RIGHT);
		tableColumnLayout.setColumnData(column, new ColumnPixelData(100));
		
		TableViewerColumn receptionColumn = new TableViewerColumn(archiveViewer, SWT.NONE);
		column = receptionColumn.getColumn();
		column.setText(Messages.BefuemView_FindingDateColumnText);
		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_RECEPTION, column, archiveViewer));
		column.setAlignment(SWT.RIGHT);
		tableColumnLayout.setColumnData(column, new ColumnPixelData(100));
		
		ObservableListContentProvider setContentProvider = new ObservableListContentProvider();
		archiveViewer.setContentProvider(setContentProvider);
		// Get the content for the viewer, setInput will call getElements in the
		// contentProvider
		IObservableMap[] observeMaps = BeansObservables.observeMaps(
				setContentProvider.getKnownElements(), NetClientFinding.class,
				NetClientFinding.archiveViewerProperties);
		archiveViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		
		List<NetClientFinding> findings = ArchiveFindingsModelProvider.getInstance().getArchiveFindings();
		ArchiveFindingsModelProvider.getInstance().updateModel(new JobFinishedListener());
		archiveViewer.setInput(new WritableList(findings, NetClientFinding.class));
		
		nameViewerColumn.setLabelProvider(new FindingPatientLabelProvider());
		typViewerColumn.setLabelProvider(new FindingTypLabelProvider());
		sizeColumn.setLabelProvider(new FindingSizeLabelProvider());
		receptionColumn.setLabelProvider(new FindingReceptionLabelProvider());
		
		archiveViewer.addDoubleClickListener(new OpenClientFindingDblClick());
		return archiveViewer;
	}
	
//	private TableViewer createOutboxViewer(Composite parent) {
//		outboxViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
//		outboxViewer.setData(VIEWER_ID, VIEWER_ID_OUTBOX);
//		tableOutbox = outboxViewer.getTable();
//		tableOutbox.setLinesVisible(true);
//		tableOutbox.setHeaderVisible(true);
//		
//		TableViewerColumn tableViewerColumn = new TableViewerColumn(outboxViewer, SWT.NONE);
//		TableColumn column = tableViewerColumn.getColumn();
//		column.setWidth(100);
//		column.setText("Empfänger");
//		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_RECEIVER, column, outboxViewer));
//		
//		tableViewerColumn = new TableViewerColumn(outboxViewer, SWT.NONE);
//		column = tableViewerColumn.getColumn();
//		column.setWidth(100);
//		column.setText("Befundtyp");
//		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_TYP, column, outboxViewer));
//		
//		tableViewerColumn = new TableViewerColumn(outboxViewer, SWT.NONE);
//		column = tableViewerColumn.getColumn();
//		column.setWidth(100);
//		column.setText("Name");
//		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_NAME, column, outboxViewer));
//		
//		TableViewerColumn sizeColumn = new TableViewerColumn(outboxViewer, SWT.NONE);
//		column = sizeColumn.getColumn();
//		column.setWidth(100);
//		column.setText("Größe");
//		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_SIZE, column, outboxViewer));
//		column.setAlignment(SWT.RIGHT);
//		
//		TableViewerColumn receptionColumn = new TableViewerColumn(outboxViewer, SWT.NONE);
//		column = receptionColumn.getColumn();
//		column.setWidth(100);
//		column.setText("Datum");
//		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_RECEPTION, column, outboxViewer));
//		
//		ObservableListContentProvider setContentProvider = new ObservableListContentProvider();
//		outboxViewer.setContentProvider(setContentProvider);
//		// Get the content for the viewer, setInput will call getElements in the
//		// contentProvider
//		IObservableMap[] observeMaps = BeansObservables.observeMaps(
//				setContentProvider.getKnownElements(), NetClientFinding.class,
//				NetClientFinding.outboxViewerProperties);
//		outboxViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
//		
//		List<NetClientFinding> findings = OutboxFindingsModelProvider.getInstance().getOutboxFindings();
//		outboxViewer.setInput(new WritableList(findings, NetClientFinding.class));
//		
//		sizeColumn.setLabelProvider(new FindingSizeLabelProvider());
//		receptionColumn.setLabelProvider(new FindingReceptionLabelProvider());
//
//		outboxViewer.addDoubleClickListener(new OpenClientFindingDblClick());
//		return outboxViewer;
//	}
	
	private TableViewer createInboxViewer(Composite parent) {
		// create a extra Composite for the table when using TableColumnLayout
		tableInbox = new Composite(parent, SWT.NONE);
		tableInbox.setLayout(new FillLayout());
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableInbox.setLayout(tableColumnLayout);
		
		inboxViewer = new TableViewer(tableInbox, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		inboxViewer.setData(VIEWER_ID, VIEWER_ID_INBOX);
		Table table = inboxViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn typViewerColumn = new TableViewerColumn(inboxViewer, SWT.NONE);
		TableColumn column = typViewerColumn.getColumn();
		column.setText(Messages.BefuemView_FindingTypColumnText);
		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_TYP, column, inboxViewer));
		tableColumnLayout.setColumnData(column, new ColumnPixelData(75));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(inboxViewer, SWT.NONE);
		column = tableViewerColumn.getColumn();
		column.setText(Messages.BefuemView_FindingSenderColumnText);
		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_SENDER, column, inboxViewer));
		tableColumnLayout.setColumnData(column, new ColumnWeightData(50, 100, true));
		
		// Label Provider will insert Patient info after finding was visited by update job
		TableViewerColumn nameViewerColumn = new TableViewerColumn(inboxViewer, SWT.NONE);
		column = nameViewerColumn.getColumn();
		column.setText(Messages.BefuemView_FindingPatientColumnText);
//		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_NAME, column, inboxViewer));
		tableColumnLayout.setColumnData(column, new ColumnWeightData(50, 100, true));
		
		TableViewerColumn sizeColumn = new TableViewerColumn(inboxViewer, SWT.NONE);
		column = sizeColumn.getColumn();
		column.setText(Messages.BefuemView_FindingSizeColumnText);
		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_SIZE, column, inboxViewer));
		column.setAlignment(SWT.RIGHT);
		tableColumnLayout.setColumnData(column, new ColumnPixelData(100));
		
		TableViewerColumn receptionColumn = new TableViewerColumn(inboxViewer, SWT.NONE);
		column = receptionColumn.getColumn();
		column.setText(Messages.BefuemView_FindingDateColumnText);
		column.setAlignment(SWT.RIGHT);
		column.addSelectionListener(new SortSelectionListener(FindingComparator.PROPERTY_RECEPTION, column, inboxViewer));
		tableColumnLayout.setColumnData(column, new ColumnPixelData(100));
		
		ObservableListContentProvider setContentProvider = new ObservableListContentProvider();
		inboxViewer.setContentProvider(setContentProvider);
		// Get the content for the viewer, setInput will call getElements in the
		// contentProvider
		IObservableMap[] observeMaps = BeansObservables.observeMaps(
				setContentProvider.getKnownElements(), NetClientFinding.class,
				NetClientFinding.inboxViewerProperties);
		inboxViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		
		List<NetClientFinding> findings = InboxFindingsModelProvider.getInstance().getInboxFindings();
		InboxFindingsModelProvider.getInstance().updateModel(new JobFinishedListener());
		inboxViewer.setInput(new WritableList(findings, NetClientFinding.class));
		
		nameViewerColumn.setLabelProvider(new FindingPatientLabelProvider());
		typViewerColumn.setLabelProvider(new FindingTypLabelProvider());
		sizeColumn.setLabelProvider(new FindingSizeLabelProvider());
		receptionColumn.setLabelProvider(new FindingReceptionLabelProvider());
		
		inboxViewer.addDoubleClickListener(new OpenClientImportDblClick());
		return inboxViewer;
	}
	
	private TableViewer createAddressbookViewer(Composite parent) {
		// create a extra Composite for the table when using TableColumnLayout
		tableAddressbook = new Composite(parent, SWT.NONE);
		tableAddressbook.setLayout(new FillLayout());
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableAddressbook.setLayout(tableColumnLayout);
		
		addressbookViewer = new TableViewer(tableAddressbook, SWT.BORDER | SWT.FULL_SELECTION);
		addressbookViewer.setData(VIEWER_ID, VIEWER_ID_ADDRESSBOOK);
		Table table = addressbookViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(addressbookViewer, SWT.NONE);
		TableColumn column = tableViewerColumn.getColumn();
		column.setText(Messages.BefuemView_ParticipantHVNbColumnText);
		tableColumnLayout.setColumnData(column, new ColumnPixelData(100));
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(addressbookViewer, SWT.NONE);
		column = tableViewerColumn_1.getColumn();
		column.setText(Messages.BefuemView_ParticipantLastnameColumnText);
		tableColumnLayout.setColumnData(column, new ColumnWeightData(33, 100, true));
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(addressbookViewer, SWT.NONE);
		column = tableViewerColumn_2.getColumn();
		column.setText(Messages.BefuemView_ParticipantFirstnameColumnText);
		tableColumnLayout.setColumnData(column, new ColumnWeightData(33, 100, true));
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(addressbookViewer, SWT.NONE);
		column = tableViewerColumn_3.getColumn();
		column.setText(Messages.BefuemView_ParticipantCityColumnText);
		tableColumnLayout.setColumnData(column, new ColumnPixelData(100));
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(addressbookViewer, SWT.NONE);
		column = tableViewerColumn_4.getColumn();
		column.setText(Messages.BefuemView_ParticipantEMailColumnText);
		tableColumnLayout.setColumnData(column, new ColumnWeightData(33, 100, true));
		
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(addressbookViewer, SWT.NONE);
		column = tableViewerColumn_5.getColumn();
		column.setText(Messages.BefuemView_ParticipantContactColumnText);
		tableColumnLayout.setColumnData(column, new ColumnWeightData(33, 100, true));
		
		ObservableListContentProvider setContentProvider = new ObservableListContentProvider();
		addressbookViewer.setContentProvider(setContentProvider);
		// Get the content for the viewer, setInput will call getElements in the
		// contentProvider
		IObservableMap[] observeMaps = BeansObservables.observeMaps(
				setContentProvider.getKnownElements(), NetworkParticipant.class,
				NetworkParticipant.addressbookViewerProperties);
		addressbookViewer.setLabelProvider(new ObservableMapLabelProvider(observeMaps));
		
		List<NetworkParticipant> persons = NetworkParticipantModelProvider.getInstance().getNetworkParticipants();
		addressbookViewer.setInput(new WritableList(persons, NetworkParticipant.class));

		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				NetworkParticipant participant = (NetworkParticipant)element;
				Kontakt kontakt = participant.getKontakt();
				if(kontakt != null)
					return kontakt.getLabel();
				else
					return ""; //$NON-NLS-1$
			}
		});
		
		return addressbookViewer;
	}
	
	private class SortSelectionListener extends SelectionAdapter {
		private int index;
		private TableColumn column;
		private TableViewer viewer;
		
		SortSelectionListener(int idx, TableColumn col, TableViewer view) {
			index = idx;
			column = col;
			viewer = view;
		}
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			comparator.setColumn(index);
			int dir = viewer.getTable().getSortDirection();
			if (viewer.getTable().getSortColumn() == column) {
				dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
			} else {
				dir = SWT.DOWN;
			}
			viewer.getTable().setSortDirection(dir);
			viewer.getTable().setSortColumn(column);
			viewer.refresh();
		}
	}
	
	private class OpenClientFindingDblClick implements IDoubleClickListener {
		@Override
		public void doubleClick(DoubleClickEvent event) {
			// Lets call our command
			IHandlerService handlerService = (IHandlerService)PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getService(IHandlerService.class);						
			try {
				handlerService.executeCommand(OpenClientFinding.ID, null);
			} catch (Exception ex) {
				throw new RuntimeException(OpenClientFinding.ID);
			}
		}
	}
	
	private class OpenClientImportDblClick implements IDoubleClickListener {
		@Override
		public void doubleClick(DoubleClickEvent event) {
			// Lets call our command
			IHandlerService handlerService = (IHandlerService)PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getService(IHandlerService.class);
			try {
				handlerService.executeCommand(OpenClientImport.ID, null);
			} catch (Exception ex) {
				throw new RuntimeException(OpenClientImport.ID);
			}
		}
	}
	
	public class JobFinishedListener extends JobChangeAdapter {
		@Override
		public void done(IJobChangeEvent event) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					if(inboxViewer.getTable() != null && !inboxViewer.getTable().isDisposed())
						inboxViewer.refresh();
					if(archiveViewer.getTable() != null && !archiveViewer.getTable().isDisposed())
						archiveViewer.refresh();
					if(addressbookViewer.getTable() != null && !addressbookViewer.getTable().isDisposed())
						addressbookViewer.refresh();
				}
			});
		}
	}
	
	private class FilterKeyListener extends KeyAdapter {
		private Text text;
		private TableViewer viewer;
		
		FilterKeyListener(Text filterTxt, TableViewer tableViewer) {
			text = filterTxt;
			viewer = tableViewer;
		}
		
		public void keyReleased(KeyEvent ke) {
			String txt = text.getText();
			if(txt.length() > 2) {
				int viewer_id = (Integer) viewer.getData(VIEWER_ID);
				if(viewer_id == VIEWER_ID_INBOX || viewer_id == VIEWER_ID_OUTBOX)
					filterNameSender.setSearchText(txt);
				else if(viewer_id == VIEWER_ID_ARCHIVE)
					filterNameSenderReceiver.setSearchText(txt);
				else if(viewer_id == VIEWER_ID_ADDRESSBOOK)
					filterParticipant.setSearchText(txt);
				viewer.refresh();
			}
			else {
				int viewer_id = (Integer) viewer.getData(VIEWER_ID);
				if(viewer_id == VIEWER_ID_INBOX || viewer_id == VIEWER_ID_OUTBOX)
					filterNameSender.setSearchText(null);
				else if(viewer_id == VIEWER_ID_ARCHIVE)
					filterNameSenderReceiver.setSearchText(null);
				else if(viewer_id == VIEWER_ID_ADDRESSBOOK)
					filterParticipant.setSearchText(null);
				viewer.refresh();
			}
		}
	}
}
