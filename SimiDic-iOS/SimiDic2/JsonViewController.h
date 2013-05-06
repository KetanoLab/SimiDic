//
//  JsonViewController.h
//  SimiDic2
//
//  Created by KetanoLab on 12/03/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface JsonViewController : UIViewController
- (IBAction)volverPrincipal:(id)sender;
@property (strong, nonatomic) IBOutlet UITableView *tablaDescargas;
@property (nonatomic) int index;
@property (nonatomic,strong) NSMutableArray *infoDiccionario;
@property (strong, nonatomic) IBOutlet UILabel *labelNomasDescargas;
@property (strong, nonatomic) NSMutableArray *swDescarga;


@property (nonatomic,strong) NSMutableData *receivedata;
@property (nonatomic,strong) NSString *nombrefile;
@property (nonatomic) int FileSize;
@property (nonatomic) float TotalByteSize;
//Variables de tipo datos para guardado en disco.

@property (nonatomic,strong) NSString *resumenDiccionario;
@property (nonatomic,strong) NSString *nombreDiccionario;
@property (nonatomic,strong) NSString *author;
@property (nonatomic,strong) NSString *descripcionDicc;
@property (nonatomic,strong) NSString *textoAlerta;

-(void)descargaNombre: (NSString *)nombre Url:(NSString *)url nombreDicc:(NSString *)nombreDicci authoDicc:(NSString *)authorDic desc:(NSString *) descripcion indiceTabla:(int)indicetabla;
@end