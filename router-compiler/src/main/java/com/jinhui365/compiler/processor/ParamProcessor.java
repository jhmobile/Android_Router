package com.jinhui365.compiler.processor;
import com.jinhui365.router.annotation.Param;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.jinhui365.compiler.utils.Consts.PARAM_ANNOTATION_TYPE;

/**
 * Name:ParamProcessor
 * Author:jmtian
 * Commemt:annotation processor of Param
 * Date: 2017/10/16 10:35
 */

@SupportedAnnotationTypes(PARAM_ANNOTATION_TYPE)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ParamProcessor extends AbstractProcessor {


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Param.class);
        if (elements == null || elements.isEmpty()) {
            return true;
        }

        return true;
    }

}
