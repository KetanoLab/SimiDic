//
//  gestionDescarga.h
//  SimiDic2
//
//  Created by KetanoLab on 20/03/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ViewController.h"

@interface gestionDescarga : NSObject
@property (nonatomic,strong) NSMutableData *receivedata;
@property (nonatomic,strong) NSString *nombrefile;
@property (nonatomic) int FileSize;
@property (nonatomic) float TotalByteSize;
@property (nonatomic,strong) NSMutableArray *infoDiccionario;
//Variables de tipo datos para guardado en disco.

@property (nonatomic,strong) NSString *resumenDiccionario;
@property (nonatomic,strong) NSString *nombreDiccionario;
@property (nonatomic,strong) NSString *author;
@property (nonatomic,strong) NSString *descripcionDicc;
@property (nonatomic,strong) NSString *textoAlerta;
@property (nonatomic,strong) NSMutableArray *sw;
-(void)descargaNombre: (NSString *)nombre Url:(NSString *)url nombreDicc:(NSString *)nombreDicci authoDicc:(NSString *)authorDic desc:(NSString *) descripcion indiceTabla:(int)indicetabla;

@end
