package com.beust.doclipse.preferences.template;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ITreeListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.TreeListDialogField;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;

import com.beust.doclipse.DoclipseProject;

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
		IProject project = DoclipseProject.getCurrentProject();
		if (index == 0) {// add javaFile
			List<Object> elements = field.getSelectedElements();
			TemplateElement templateElement = (TemplateElement) elements.get(0);
			if (templateElement.getKind() == IClasspathEntry.CPE_PROJECT) {// 给项目添加java文件

			}
			final ResourcesSelectionDialog dialog = new ResourcesSelectionDialog(Display.getCurrent().getActiveShell(),
					true, project, IResource.FILE, new FileExtensionFilter("java"));
			final int resultCode = dialog.open();
			final Object[] result = dialog.getResult();
			if (resultCode != Window.OK || result == null) {
				return;
			}
			
			for (int i = 0; i < result.length; i++) {
				IResource resource = (IResource) result[i];
				IJavaElement javafile = JavaCore.create(resource);
				ICompilationUnit compilationUnit = (ICompilationUnit) javafile;
				ASTParser parser = ASTParser.newParser(AST.JLS8);
				parser.setSource(compilationUnit.getContents());
				org.eclipse.jdt.core.dom.CompilationUnit astnode = (org.eclipse.jdt.core.dom.CompilationUnit) parser
						.createAST(null);
				TemplateElement pakage=this.getOrCreatePackageElement(templateElement,astnode.getPackage().getName().toString());
				TemplateElement file=new TemplateElement();
				file.setText(resource.getName());
				file.setKind(TemplateElement.CPE_JAVA);
				pakage.getChildren().add(file);
			}

			field.refresh();
		}
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
			if (templateElement.getKind() == IClasspathEntry.CPE_PROJECT) {// 选中project
																			// 那么添加类有效
				field.enableButton(0, true);
			}
		}
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
