package com.parting_soul.compiler;

import com.google.auto.service.AutoService;
import com.parting_soul.annotation.Test;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * 自定义注解处理器
 */
@AutoService(Processor.class)
public class CoreProcessor extends AbstractProcessor {

    /**
     * 用于获取  包，类元素节点信息
     */
    private Elements mElementUtils;


    /**
     * 用于操作类型元素的工具类
     */
    private Types mTypeUtils;

    /**
     * 用来输出日志
     */
    private Messager mMessager;

    /**
     * 文件生成器
     */
    private Filer mFiler;


    /**
     * 初始化
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
        mTypeUtils = processingEnvironment.getTypeUtils();

        Map<String, String> options = processingEnvironment.getOptions();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "======>>>" + options.get("content"));
    }


    /**
     * 指定java支持的版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }


    /**
     * 指定处理的注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Test.class.getCanonicalName());
    }

    /**
     * 注解处理器支持的参数
     *
     * @return
     */
    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    /**
     * 注解的处理
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty()) {
            return false;
        }
        //得到所有被Test注解修饰的类元素
        Set<? extends Element> sets = roundEnvironment.getElementsAnnotatedWith(Test.class);
        for (Element e : sets) {
            //1. 得到当前被Test注解修饰类的包元素
            PackageElement packageElement = mElementUtils.getPackageOf(e);
            //得到包元素的名称
            String packageName = packageElement.getQualifiedName().toString();
            //得到当前类的名称
            String targetClassName = e.getSimpleName().toString();
            //新生成类的名称
            String newClassName = targetClassName + "$Test";

            //java文件对象
            Writer writer = null;
            try {
                JavaFileObject fileObject = mFiler.createSourceFile(packageName + "." + newClassName);
                writer = fileObject.openWriter();
                //将需要自动生成的内容通过流写入文件
                writer.write("package " + packageName + ";\n");
                writer.write("public class " + newClassName + " {\n");
                writer.write("public String methodTest() {\n");
                writer.write("return \"APT 测试样例\";\n");
                writer.write("}\n");
                writer.write("}\n");
                mMessager.printMessage(Diagnostic.Kind.NOTE, "====>>>" + packageName);
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }

        }
        return true;
    }

}
