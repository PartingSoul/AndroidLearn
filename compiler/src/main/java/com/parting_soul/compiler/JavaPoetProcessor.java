package com.parting_soul.compiler;

import com.google.auto.service.AutoService;
import com.parting_soul.annotation.Hello;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author parting_soul
 * @date 2019-11-26
 */
@AutoService(Processor.class)
public class JavaPoetProcessor extends AbstractProcessor {

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
        return Collections.singleton(Hello.class.getCanonicalName());
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


        FieldSpec fieldSpec = FieldSpec.builder(TypeName.LONG, "timeStamp", Modifier.PRIVATE).build();
        FieldSpec fieldSpec1 = FieldSpec.builder(String.class, "name", Modifier.PRIVATE).build();

        //方法
        MethodSpec main = MethodSpec
                //方法名
                .methodBuilder("main")
                //修饰符
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                //形参
                .addParameter(String[].class, "args")
                //语句
                .addStatement("$T.out.println($S);", System.class, "hello javaPoet!")
                //返回值
                .returns(TypeName.VOID)
                .build();

        AnnotationSpec a = AnnotationSpec.builder(Dog.class)
                .addMember("a", "", "")
                .build();
        // 类
        TypeSpec clazz = TypeSpec
                .classBuilder("Hello")
                //类的修饰符
                .addField(fieldSpec)
                .addField(fieldSpec1)
                .addAnnotation(a)
                .addModifiers(Modifier.PUBLIC)
                //为类添加方法
                .addMethod(main)
                .build();

        //Java文件
        JavaFile javaFile = JavaFile.builder("com.parting_soul.learn", test$T())
                .indent("\t")
                .build();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "ddd");
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * FieldSpec 用于生成属性
     *
     * @return
     */
    public TypeSpec testFieldSpec() {
        FieldSpec fieldSpec = FieldSpec.builder(TypeName.LONG, "timeStamp", Modifier.PRIVATE)
                .initializer("$L", 1000).build();
        FieldSpec fieldSpec1 = FieldSpec.builder(String.class, "name", Modifier.PRIVATE).build();
        // 类
        return TypeSpec
                .classBuilder("FieldSpecTest")
                //类的修饰符
                .addField(fieldSpec)
                .addField(fieldSpec1)
                .build();
    }

    /**
     * MethodSpec 用于生成方法
     *
     * @return
     */
    public TypeSpec testMethodSpec() {
        ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.LONG, "timeStamp").build();
        ParameterSpec parameterSpec1 = ParameterSpec.builder(String.class, "name").build();

        MethodSpec methodSpec = MethodSpec.methodBuilder("testMethodSpec")
                .addParameter(parameterSpec)
                .addParameter(parameterSpec1)
                .returns(TypeName.VOID)
                .addModifiers(Modifier.FINAL)

                //语句
                .addStatement("System.out.println(123)")
                //代码块
                .addCode(" for (int i = 0; i < 10; i++) {\n" +
                        "            System.out.print(i);\n" +
                        "        }")
                .addCode(CodeBlock.builder()
                        .add("System.out.println(\"CodeBlock\");")
                        .build())

//                //{代码}
                .beginControlFlow("for (int i = 0; i < 10; i++)")
                .addStatement(" System.out.print(i)")
                .endControlFlow()

                .addStatement("int b = 0 ")
                .beginControlFlow("if(b == 1)")
                .nextControlFlow("else if (b == 2)")
                .endControlFlow()
                .addParameter(String[].class, "vargs")
                .varargs()
                .build();

        // 类
        return TypeSpec
                .classBuilder("MethodSpecTest")
                .addMethod(methodSpec)
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    /**
     * ParameterSpec 用于生成方法形参
     *
     * @return
     */
    public TypeSpec testParameterSpec() {
        ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.LONG, "timeStamp").build();
        ParameterSpec parameterSpec1 = ParameterSpec.builder(String.class, "name").build();

        MethodSpec methodSpec = MethodSpec.methodBuilder("testMethodSpec")
                .addParameter(parameterSpec)
                .addParameter(parameterSpec1)
                .build();

        // 类
        return TypeSpec
                .classBuilder("MethodSpecTest")
                .addMethod(methodSpec)
                .addModifiers(Modifier.PUBLIC)
                .build();
    }


    public TypeSpec test$L() {
        MethodSpec methodSpec = MethodSpec.methodBuilder("test$L")
                .beginControlFlow("for(int i = $L;i < $L;i++)", 0, 10)
                .addStatement("System.out.println(i)")
                .endControlFlow()
                .addStatement("System.out.println($S)", "Hello JavaPoet")
                .build();

        // 类
        return TypeSpec
                .classBuilder("Test$L")
                .addMethod(methodSpec)
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    public TypeSpec test$N() {
        ParameterSpec parameterSpec1 = ParameterSpec.builder(String.class, "name").build();

        MethodSpec methodSpec = MethodSpec.methodBuilder("test$N")
                .addParameter(parameterSpec1)
                .addStatement("$N = $S", parameterSpec1, "参数引用")
                .build();

        // 类
        return TypeSpec
                .classBuilder("Test$N")
                .addMethod(methodSpec)
                .addModifiers(Modifier.PUBLIC)
                .build();
    }


    public TypeSpec test$T() {
        MethodSpec today = MethodSpec.methodBuilder("today")
                .returns(Date.class)
                .addStatement("return new $T()", Date.class)
                .build();

        return TypeSpec.classBuilder("test$T")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(today)
                .build();
    }
}
