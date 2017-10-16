package com.jinhui365.compiler.processor;
import com.jinhui365.router.annotation.InjectContext;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.jinhui365.compiler.utils.Consts.CONTEXT_ANNOTATION_TYPE;


/**
 * Name:RouteContextProcessor
 * Author:jmtian
 * Commemt:annotation processor of InjectContext
 * Date: 2017/10/16 10:33
 */

@SupportedAnnotationTypes(CONTEXT_ANNOTATION_TYPE)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RouteContextProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

    }

    /**
     * This method will be called some times.
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(InjectContext.class);


        return true;
    }
}
