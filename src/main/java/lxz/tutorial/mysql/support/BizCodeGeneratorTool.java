package lxz.tutorial.mysql.support;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import lxz.tutorial.mysql.domain.Product;
import org.springframework.util.SystemPropertyUtils;

public class BizCodeGeneratorTool {

  public static final String HTML_INPUT = "<s:textfield cssClass=\"infoTableInput\" name=\"${model}.${field}\" id=\"${model}_${field}\" maxlength=\"32\"></s:textfield>";

  public static void main(String[] args) throws Exception {
		generateServiceFile(Product.class, "产品");
//		generateAction(Doctor.class, "医生");
//    generateInputField(Product.class);
  }

  /**
   *
   * @param clazz
   *
   */
  public static <T> void generateInputField(Class<T> clazz) {
    String className = clazz.getSimpleName();
    String model = className.substring(0, 1).toLowerCase().concat(className.substring(1));
    System.setProperty("model", model);
    for (Field f : clazz.getDeclaredFields()) {
      String field = f.getName();
      System.setProperty("field", field);
      String result = SystemPropertyUtils.resolvePlaceholders(HTML_INPUT);
      System.out.println(result);
    }
  }

  public static <T> void generateServiceFile(Class<T> clazz, String comment) throws Exception {

		/*
     * ---------------------------------------------------------------------
		 * ---
		 */
    /* You should do this ONLY ONCE in the whole application life-cycle: */

		/* Create and adjust the configuration singleton */
    Configuration cfg = new Configuration();
    cfg.setDirectoryForTemplateLoading(new File(MybatisGeneratorTool.classPath + "/freemarker"));
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

    String className = clazz.getSimpleName();
    String vo = Character.toLowerCase(className.charAt(0)) + className.substring(1);
		String voPackage = clazz.getPackage().getName();
		String servicePackage = voPackage.substring(0, voPackage.lastIndexOf("."))+".service";
		String serviceImplPackage = servicePackage + ".impl";
		String mapperPackage =voPackage.substring(0, voPackage.lastIndexOf("."))+".mapper";
		/* Create a data-model */
    Map root = new HashMap();
    root.put("voClassSimpleName", clazz.getSimpleName());
    root.put("voComment", comment);
    root.put("vo", vo);
    root.put("voPackage", voPackage);
    root.put("servicePackage", servicePackage);
    root.put("serviceImplPackage", serviceImplPackage);
    root.put("mapperPackage", mapperPackage);

		/* Get the template (uses cache internally) */
    Template serviceTmpl = cfg.getTemplate("Service.ftl");
    Template serviceImplTmpl = cfg.getTemplate("ServiceImpl.ftl");
    String modelPath =
        MybatisGeneratorTool.projectPath + "/src/main/java/" + clazz.getPackage().getName()
            .replaceAll("\\.", "/");
    String serviceInterfacePath =
        modelPath.substring(0, modelPath.indexOf("/domain")) + "/service/" + className
            + "Service.java";
    String serviceImplPath =
        modelPath.substring(0, modelPath.indexOf("/domain")) + "/service/impl/" + className
            + "ServiceImpl.java";
		/* Merge data-model with template */
    Writer serviceOut = new OutputStreamWriter(new FileOutputStream(serviceInterfacePath));
    serviceTmpl.process(root, serviceOut);

    Writer serviceImplOut = new OutputStreamWriter(new FileOutputStream(serviceImplPath));
    serviceImplTmpl.process(root, serviceImplOut);

    // Note: Depending on what `out` is, you may need to call `out.close()`.
    // This is usually the case for file output, but not for servlet output.
  }


  public static <T> void generateAction(Class<T> clazz, String comment) throws Exception {

    Configuration cfg = new Configuration();
    cfg.setDirectoryForTemplateLoading(new File(MybatisGeneratorTool.classPath + "/freemarker"));
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

    String className = clazz.getSimpleName();
    String vo = Character.toLowerCase(className.charAt(0)) + className.substring(1);
		
		/* Create a data-model */
    Map root = new HashMap();
    root.put("voClassSimpleName", clazz.getSimpleName());
    root.put("voComment", comment);
    root.put("vo", vo);

		/* Get the template (uses cache internally) */
    Template actionTmpl = cfg.getTemplate("Action.ftl");
    String modelPath =
        MybatisGeneratorTool.projectPath + "/src/main/java/" + Product.class.getPackage().getName()
            .replaceAll("\\.", "/");
    String actionDir =
        modelPath.substring(0, modelPath.indexOf("/models")) + "/actions/" + className
            .toLowerCase();
    File f = new File(actionDir);
    if (!f.exists()) {
      f.mkdir();
    }
    String actionPath = actionDir + "/" + className + "Action.java";
		
		/* Merge data-model with template */
    Writer serviceOut = new OutputStreamWriter(new FileOutputStream(actionPath));
    actionTmpl.process(root, serviceOut);

  }
}
