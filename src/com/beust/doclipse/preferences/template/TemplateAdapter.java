package com.beust.doclipse.preferences.template;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ITreeListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.TreeListDialogField;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;

import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.preferences.template.dialog.TemplateDialog;

/**
 * 模版页
 * 
 * @author myking520
 *
 */
public class TemplateAdapter implements ITreeListAdapter<TemplateElement> {
	private TemplatePage templatePage;

	public TemplateAdapter(TemplatePage templatePage) {
		super();
		this.templatePage = templatePage;
	}

	@Override
	public void customButtonPressed(TreeListDialogField<TemplateElement> field, int index) {
		if (index == 0) {// add javaFile
			this.addJavaFile(field);
			return;
		}
		if(index==1){
			this.addTemplate(field);
			return;
		}
		if(index==2){
			this.edit(field);
		}
		if(index==3){
			this.del(field);
			return;
		}
	}
	private void edit(TreeListDialogField<TemplateElement> field){
		List<Object> elements = field.getSelectedElements();
		TemplateElement element=(TemplateElement) elements.get(0);
		TemplateElement engine=null;
		TemplateElement javefile=null;
		if(element.getKind()==TemplateElement.CPE_ENGINE||element.getKind()==TemplateElement.CPE_EXPORT){
			engine=templatePage.getTemplateElementProvider().getElementRoot().getParent(element);
		}
		if(element.getKind()==TemplateElement.CPE_IMPORT){
			engine=element;	
		}
		javefile=templatePage.getTemplateElementProvider().getElementRoot().getParent(engine);
		TemplateDialog dialog=new TemplateDialog(Display.getCurrent().getActiveShell(),javefile,engine);
		int code=dialog.open();
		field.refresh();
	}
	private void addTemplate(TreeListDialogField<TemplateElement> field){
		List<Object> elements = field.getSelectedElements();
		TemplateElement element=(TemplateElement) elements.get(0);
		TemplateDialog dialog=new TemplateDialog(Display.getCurrent().getActiveShell(),element,null);
		int code=dialog.open();
		field.refresh();
	}
	private void del(TreeListDialogField<TemplateElement> field){
		List<Object> elements = field.getSelectedElements();
		for(int i=0;i<elements.size();i++){
			TemplateElement element=	(TemplateElement) elements.get(i);
			templatePage.getTemplateElementProvider().getElementRoot().remove(element);
		}
		templatePage.getTemplateElementProvider().saveOrUpdate();
		field.refresh();
	}
	private void addJavaFile(TreeListDialogField<TemplateElement> field){
		IProject project = DoclipseProject.getCurrentProject();
		final ResourcesSelectionDialog dialog = new ResourcesSelectionDialog(Display.getCurrent().getActiveShell(),
				true, project, IResource.FILE, new FileExtensionFilter("java",templatePage));
		final int resultCode = dialog.open();
		final Object[] result = dialog.getResult();
		if (resultCode != Window.OK || result == null) {
			return;
		}
		TemplateElement root=templatePage.getTemplateElementProvider().getElementRoot();
		for(int i=0;i<result.length;i++){
			IResource resource = (IResource) result[i];
			String text=resource.getFullPath().toString();
			if(root.getByText(text)!=null){
				continue;
			}
			TemplateElement fileElement=new TemplateElement(text);
			fileElement.setKind(TemplateElement.CPE_JAVA);
			TemplateDialog templateDialog=new TemplateDialog(Display.getCurrent().getActiveShell(), fileElement,null);
			if(templateDialog.open()!=Window.OK){
				continue;
			}
			String folder=resource.getParent().getFullPath().makeRelative().toString();
			TemplateElement folderElement=root.getChildren(folder);
			if(folderElement==null){
				folderElement=new TemplateElement(folder);
				folderElement.setKind(TemplateElement.CPE_PACKAGE);
				root.getChildren().add(folderElement);
			}

			folderElement.getChildren().add(fileElement);
		}
		templatePage.getTemplateElementProvider().saveOrUpdate();
		field.refresh();
	}
	public TemplateElement getOrCreatePackageElement(TemplateElement templateElement, String packageName) {
		if (templateElement.getKind() == TemplateElement.CPE_PROJECT) {
			return getOrCreatePackageElementOnProject(templateElement, packageName);
		}
		if(templateElement.getKind()==TemplateElement.CPE_PACKAGE){
			return templateElement;
		}
		templateElement=	templatePage.getTreeListDialogField().getElements().get(0);
		return getOrCreatePackageElementOnProject(templateElement, packageName);
	}
	public TemplateElement getOrCreatePackageElementOnProject(TemplateElement templateElement, String packageName){
			for (int i = 0; i < templateElement.getChildren().size(); i++) {
				TemplateElement tmptemplateElement=templateElement.getChildren().get(i);
				if(tmptemplateElement.getKind()==TemplateElement.CPE_PACKAGE){
					if(packageName.equals(tmptemplateElement.getText())){
						return tmptemplateElement;
					}
				}
			}
			TemplateElement ptemplateElement=new TemplateElement();
			ptemplateElement.setText(packageName);
			ptemplateElement.setKind(TemplateElement.CPE_PACKAGE);
			templateElement.getChildren().add(ptemplateElement);
			return ptemplateElement;
	}
	@Override
	public void selectionChanged(TreeListDialogField<TemplateElement> field) {
		List<Object> elements = field.getSelectedElements();
		for (int i = 0; i < templatePage.buttonLabels.length; i++) {
			field.enableButton(i, false);
		}
		if (elements.size() == 1) {
			TemplateElement templateElement = (com.beust.doclipse.preferences.template.TemplateElement) elements.get(0);
			if (templateElement.getKind() == TemplateElement.CPE_PROJECT||templateElement.getKind() == TemplateElement.CPE_PACKAGE) {
				field.enableButton(0, true);
				if(templateElement.getKind()==TemplateElement.CPE_PROJECT){
					return;
				}
				field.enableButton(3, true);
				return;
			}
			
			if (templateElement.getKind() == TemplateElement.CPE_JAVA) {
				field.enableButton(1, true);
				field.enableButton(3, true);
			}
			if(templateElement.getKind()==TemplateElement.CPE_IMPORT){
				field.enableButton(2, true);
				field.enableButton(3, true);
				return;
			}
			if(templateElement.getKind()==TemplateElement.CPE_ENGINE||templateElement.getKind()==TemplateElement.CPE_EXPORT){
				field.enableButton(2, true);
			}
			return;
		}
		field.enableButton(4, true);
	}

	@Override
	public void doubleClicked(TreeListDialogField<TemplateElement> field) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(TreeListDialogField<TemplateElement> field, KeyEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getChildren(TreeListDialogField<TemplateElement> field, Object element) {
		TemplateElement templateElement = (TemplateElement) element;
		if (templateElement.getChildren() == null) {
			return null;
		}
		return templateElement.getChildren().toArray();
	}

	@Override
	public Object getParent(TreeListDialogField<TemplateElement> field, Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(TreeListDialogField<TemplateElement> field, Object element) {
		TemplateElement templateElement = (TemplateElement) element;
		return templateElement.getChildren() != null && templateElement.getChildren().size() > 0;
	}

}
