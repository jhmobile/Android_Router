package com.jinhui365.compiler.processor;
import com.jinhui365.router.annotation.Interceptor;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.jinhui365.compiler.utils.Consts.INTERCEPTOR_ANNOTATION_TYPE;

/**
 * Name:InterceptorProcessor
 * Author:jmtian
 * Commemt:annotation processor of Interceptor
 * Date: 2017/10/16 10:36
 */

@SupportedAnnotationTypes(INTERCEPTOR_ANNOTATION_TYPE)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class InterceptorProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Interceptor.class);
        if (elements == null || elements.isEmpty()) {
            return true;
        }

        return true;
    }
}
