package com.linearity.opentc4;//package vazkii.botania.common.annotations;//yes i stole code

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A purely-documentative annotation.
 * This annotation is used by developers in xplat code. The annotated method is intended
 * to "soft implement" a certain method in a loader specific interface that cannot be
 * named in xplat code and thus cannot be checked with @Override.
 * In this context, "soft implement" means to implement the method by matching the signature
 * with the intended interface method.
 * Examples of interfaces that we would use this for is IForgeItem or FabricItem.
 *
 * The intent is that we audit such sites every major Minecraft version or so, to ensure
 * that they still properly override the intended target.
 *///doc from Botania source(in fact we dont call "xplat" here we call "common"(architectury) )
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface SoftImplement {
    /**
     * What interface we're soft implementing
     */
    String value();
}
//is there anyone made an IDEA plugin to redirect #value() to original method? TODO:[maybe wont finished]IDEA plugin to redirect to original method