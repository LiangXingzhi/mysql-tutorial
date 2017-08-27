package lxz.tutorial.mysql.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class MySQLLimitPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> list) {
		return true;
	}

	/**
	 * 为每个Example类添加limit和offset属性已经set、get方法
	 */
	@Override
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

		PrimitiveTypeWrapper integerWrapper = FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper();

		Field limit = new Field();
		limit.setName("limit");
		limit.setVisibility(JavaVisibility.PRIVATE);
		limit.setType(integerWrapper);
		topLevelClass.addField(limit);

		Field offset = new Field();
		offset.setName("offset");
		offset.setVisibility(JavaVisibility.PRIVATE);
		offset.setType(integerWrapper);
		topLevelClass.addField(offset);

		Method constructor = new Method();
		constructor.setConstructor(true);
		constructor.setName(topLevelClass.getType().getShortName());
		constructor.addParameter(new Parameter(integerWrapper, "limit"));
		constructor.addParameter(new Parameter(integerWrapper, "offset"));
		constructor.setVisibility(JavaVisibility.PUBLIC);
		constructor.addBodyLine("this();");
		constructor.addBodyLine("this.limit = limit;");
		constructor.addBodyLine("this.offset = offset;");
		topLevelClass.addMethod(constructor);

		Method setLimit = new Method();
		setLimit.setVisibility(JavaVisibility.PUBLIC);
		setLimit.setName("setLimit");
		setLimit.addParameter(new Parameter(integerWrapper, "limit"));
		setLimit.addBodyLine("this.limit = limit;");
		topLevelClass.addMethod(setLimit);

		Method getLimit = new Method();
		getLimit.setVisibility(JavaVisibility.PUBLIC);
		getLimit.setReturnType(integerWrapper);
		getLimit.setName("getLimit");
		getLimit.addBodyLine("return limit;");
		topLevelClass.addMethod(getLimit);

		Method setOffset = new Method();
		setOffset.setVisibility(JavaVisibility.PUBLIC);
		setOffset.setName("setOffset");
		setOffset.addParameter(new Parameter(integerWrapper, "offset"));
		setOffset.addBodyLine("this.offset = offset;");
		topLevelClass.addMethod(setOffset);

		Method getOffset = new Method();
		getOffset.setVisibility(JavaVisibility.PUBLIC);
		getOffset.setReturnType(integerWrapper);
		getOffset.setName("getOffset");
		getOffset.addBodyLine("return offset;");
		topLevelClass.addMethod(getOffset);

		return true;
	}

	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType repo = new FullyQualifiedJavaType(
				"lxz.tutorial.mysql.domain.BaseDomain");
		topLevelClass.addImportedType(repo);
		topLevelClass.setSuperClass(repo);
		return true;
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();
		for (GeneratedJavaFile javaFile : introspectedTable.getGeneratedJavaFiles()) {
			CompilationUnit unit = javaFile.getCompilationUnit();
			String shortName = unit.getType().getShortName();
			if (shortName.endsWith("Mapper")) { // CRUD Mapper
				if (javaFile.getCompilationUnit() instanceof Interface) {
					Interface mapperInterface = (Interface) javaFile.getCompilationUnit();
					FullyQualifiedJavaType repo = new FullyQualifiedJavaType(
							Mapper.class.getName());
					mapperInterface.addImportedType(repo);
					mapperInterface.addAnnotation("@Mapper");
					mapperJavaFiles.add(javaFile);

				}

			}
		}
		return mapperJavaFiles;
	}

	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
		List<GeneratedXmlFile> xmlFiles = new ArrayList<GeneratedXmlFile>();
		for (GeneratedXmlFile xml : introspectedTable.getGeneratedXmlFiles()) {
			xmlFiles.add(xml);
			try {
				java.lang.reflect.Field f = GeneratedXmlFile.class.getDeclaredField("document");
				f.setAccessible(true);
				Document doc = (Document) f.get(xml);
				xmlFiles.add(new GeneratedXmlFile(doc, xml.getFileName(), xml.getTargetPackage(),
						xml.getTargetProject(), false, context.getXmlFormatter()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return xmlFiles;
	}

	/**
	 * 为Mapper.xml的selectByExample添加limit
	 */
	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {

		XmlElement ifLimitNotNullElement = new XmlElement("if");
		ifLimitNotNullElement.addAttribute(new Attribute("test", "limit != null"));

		XmlElement ifOffsetNotNullElement = new XmlElement("if");
		ifOffsetNotNullElement.addAttribute(new Attribute("test", "offset != null"));
		ifOffsetNotNullElement.addElement(new TextElement("limit ${offset}, ${limit}"));
		ifLimitNotNullElement.addElement(ifOffsetNotNullElement);

		XmlElement ifOffsetNullElement = new XmlElement("if");
		ifOffsetNullElement.addAttribute(new Attribute("test", "offset == null"));
		ifOffsetNullElement.addElement(new TextElement("limit ${limit}"));
		ifLimitNotNullElement.addElement(ifOffsetNullElement);

		element.addElement(ifLimitNotNullElement);

		return true;
	}
}