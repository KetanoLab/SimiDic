//
//  ViewController.h
//  SimiDic2
//
//  Created by KetanoLab on 12/03/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "JsonViewController.h"
#import "sqlite3.h"
#import "VistaViewController.h"
#import "CreditosIIViewController.h"

@interface ViewController : UIViewController <UITextFieldDelegate>
//Tabla de resultados de una busqueda.
@property (strong, nonatomic) IBOutlet UITableView *busquedaDiccionario;
//Botones de cambio de pantallas.
- (IBAction)irDiccionarios:(id)sender;
- (IBAction)irDescargas:(id)sender;
//Campo de texto asociado a una funcion que busca el contenido de la caja de texto.
- (IBAction)buscadorSqlite:(id)sender;
//Propiedades de elementos declarados como salidas para tener acceso a los datos que contiene.
@property (strong, nonatomic) IBOutlet UITextField *textBusqueda;
@property (strong, nonatomic) IBOutlet UITableView *tabladeDiccionarios;
@property (strong, nonatomic) NSString *author;
//Boton encargado de mostrar la tabla con los datos de los diccionarios que hayan sido descagados, para su respectivo Cambio.
- (IBAction)cambiarDiccionario:(id)sender;
//Propiedad de el campo de texto que contiene la palabra a ser buscada.
@property (strong, nonatomic) IBOutlet UIButton *textoDiccionario;
//Array de datos provenientes de la lectura del archivo plist almacenado en disco.
@property (strong, nonatomic) NSMutableArray *datosDiccionario;
//Botones de cambio de pantallas.
- (IBAction)irFavoritos:(id)sender;
//Propiedad de la vista splash que aparece al inicio de la aplicacion.
@property (strong, nonatomic) IBOutlet UIView *vistaSplah;
//Botones de cambio de pantallas.
- (IBAction)irCreditos:(id)sender;
@end
