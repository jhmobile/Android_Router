package com.jinhui365.compiler.processor;
import com.jinhui365.router.annotation.Route;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import static com.jinhui365.compiler.utils.Consts.ROUTE_ANNOTATION_TYPE;


/**
 * Name:RouteProcessor
 * Author:jmtian
 * Commemt:annotation processor of Route
 * Date: 2017/10/16 10:38
 */

@SupportedAnnotationTypes(ROUTE_ANNOTATION_TYPE)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RouteProcessor extends AbstractProcessor {


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

    }

    /**
     * This method will be called some times.
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Route.class);
        if (elements == null || elements.isEmpty()) {
            return true;
        }

        return true;
    }
}
