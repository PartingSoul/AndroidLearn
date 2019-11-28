package com.parting_soul.compiler;

import com.parting_soul.annotation.Test;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * 自定义注解处理器
 *
 * @author parting_soul
 * @date 2019-11-21
 */
public class CustomProcessor extends AbstractProcessor {

    /**
     * 用来输出日志
     */
    private Messager mMessager;

    /**
     * 处理注解前做一些初始化操作，一般是用来初始化有些工具类
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
    }


    /**
     * 指定从外部传入的参数key集合
     *
     * @return
     */
    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    /**
     * 指定要处理的注解集合
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    /**
     * 指定使用的JDK版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    /**
     * 处理注解
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> sets = roundEnvironment.getElementsAnnotatedWith(Test.class);
        for (Element e : sets) {
            StringBuilder builder = new StringBuilder();
            builder.append("--------").append("\n")
                    .append("getModifiers() ").append(e.getModifiers()).append("\n")
                    .append("getEnclosingElement() ").append(e.getEnclosingElement()).append("\n")
                    .append("getKind() ").append(e.getKind()).append("\n")
                    .append("getSimpleName() ").append(e.getSimpleName()).append("\n")
                    .append("getAnnotation() ").append(e.getAnnotation(Test.class)).append("\n")
                    .append("getAnnotationMirrors() ").append(e.getAnnotationMirrors()).append("\n")
                    .append("--------").append("\n");
            mMessager.printMessage(Diagnostic.Kind.NOTE, builder);

        }
        return true;
    }

}
