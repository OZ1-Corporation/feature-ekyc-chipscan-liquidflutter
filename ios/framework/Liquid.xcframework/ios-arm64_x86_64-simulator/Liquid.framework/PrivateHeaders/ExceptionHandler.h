//
//  ExceptionHandler.m
//  Liquid
//
//  Created by daisuke ebina on 2021/06/12.
//  Copyright © 2021 Liquid, Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
@interface ExceptionHandler : NSObject
+ (BOOL)catchExceptionWithTryBlock:(__attribute__((noescape)) void(^ _Nonnull)(void))tryBlock error:(NSError * _Nullable __autoreleasing * _Nullable)error;
@end
