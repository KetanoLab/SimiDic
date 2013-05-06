//
//  ViewController.m
//  SimiDic2
//
//  Created by KetanoLab on 12/03/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import "ViewController.h"
#import "DatosViewController.h"
#import "sqlite3.h"
#import "DiccionariosViewController.h"

@interface ViewController ()
//Array que contiene los datos de una busqueda.
@property (nonatomic,strong) NSMutableArray *valoresBusqueda;
//Cadena que contiene la clave del diccionario actual.
@property (nonatomic,strong) NSString *diccionarioActual;
@end

@implementation ViewController
@synthesize textBusqueda,valoresBusqueda,busquedaDiccionario,textoDiccionario;
@synthesize datosDiccionario,tabladeDiccionarios,diccionarioActual,author;
@synthesize vistaSplah;

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self leer];
    [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(iniciarVistaAplicaion) userInfo:nil repeats:NO];
}
-(void)iniciarVistaAplicaion{
    vistaSplah.hidden = YES;
    self.tabladeDiccionarios.hidden=YES;
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)executeSentence:(NSString *)sentence sentenceIsSelect:(BOOL )isSelect{
	
	// Variables para realizar la consulta
	static sqlite3 *db;
	sqlite3_stmt *resultado;
	const char* siguiente;
    
	// Buscar el archivo de base de datos
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *path = [documentsDirectory stringByAppendingPathComponent:self.diccionarioActual];
    
	// Abre el archivo de base de datos
	if (sqlite3_open([path UTF8String], &db) == SQLITE_OK) {
		
        self.valoresBusqueda =[[NSMutableArray alloc] init];
		if (isSelect){
            NSString *consulta = @"select word,meaning,summary from words where words.word like ";
            NSString *t = @"%";
            NSString *consultafinal = [[NSString alloc] initWithFormat:@"%@\"%@%@\" limit 10",consulta,sentence,t];
            NSLog(@"%@",consultafinal);
			// Ejecuta la consulta
			if ( sqlite3_prepare_v2(db,[consultafinal UTF8String],-1,&resultado,NULL) == SQLITE_OK ){
				// Recorre el resultado
				while (sqlite3_step(resultado)==SQLITE_ROW){
                    
                    NSString * a = [NSString stringWithUTF8String: (char *)sqlite3_column_text(resultado, 0)];
                    NSLog(@"%@",a);
                    NSString * b = [NSString stringWithUTF8String: (char *)sqlite3_column_text(resultado, 1)];
                    NSLog(@"%@",b);
                    NSString * cc = [NSString stringWithUTF8String: (char *)sqlite3_column_text(resultado, 2)];
                    NSLog(@"%@",cc);
                    NSDictionary *valoresVista = [[NSDictionary alloc]initWithObjectsAndKeys:a,@"word",b,@"meaning",cc,@"summary", nil];
                    [self.valoresBusqueda addObject:valoresVista];
         		}
			}else{
                NSLog(@"error");
            }
		}
		else {
			// Ejecuta la consulta
			if ( sqlite3_prepare_v2(db,[sentence UTF8String],[sentence length],&resultado,&siguiente) == SQLITE_OK ){
				sqlite3_step(resultado);
				sqlite3_finalize(resultado);
			}
		}
	}
	// Cierra el archivo de base de datos
	sqlite3_close(db);
}

// Método necesario para devolver el número de secciones de la tabla, por lo general será siempre 1..
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}
// Necesario para indicar el número de filas de la tabla, esto suele ir ligado al tamaño de un array de elementos a mostrar..
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
if (self.tabladeDiccionarios == tableView) {
    return [self.datosDiccionario count];
}
else{   return [self.valoresBusqueda count];
}}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.tabladeDiccionarios == aTableView) {
        
        // Normalmente recuperaremos del array, según la posicion de la fila..
        NSDictionary *aux = [self.datosDiccionario objectAtIndex:indexPath.row];
        // Creamos la celda (o fila).
        UITableViewCell *cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"Cell"];
        // Y le establecemos el texto de nuestro dato a una de las filas.
        cell.textLabel.textColor = [UIColor whiteColor];
        cell.textLabel.text =[aux objectForKey:@"nombreDicc"];
        // Y finalizamos devolviendo la celda
        return cell;
        
    }
    else{
    // Normalmente recuperaremos del array, según la posicion de la fila..
    NSDictionary *datoString = [self.valoresBusqueda objectAtIndex:indexPath.row];
    // Creamos la celda (o fila).
    UITableViewCell *cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"Cell"];
    // Y le establecemos el texto de nuestro dato a una de las filas.
    cell.textLabel.text =[datoString objectForKey:@"word"];
    cell.detailTextLabel.text =[datoString objectForKey:@"summary"];
    // Y finalizamos devolviendo la celda
    return cell;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.tabladeDiccionarios == tableView) {
        //Genaracion de los metodos que haremos para la vista de la tabla qu podremos elegir.
        self.tabladeDiccionarios.hidden = YES;
        NSDictionary *aux = [self.datosDiccionario objectAtIndex:indexPath.row];
        [textoDiccionario setTitle:[aux objectForKey:@"nombreDicc"] forState:UIControlStateNormal];
        self.diccionarioActual = [aux objectForKey:@"nombrefile"];
        self.author = [aux objectForKey:@"author"];
        if ([self.textBusqueda.text isEqualToString:@""]) {
            NSLog(@"opcion vacia");
        }
        else{
            
            [self executeSentence:self.textBusqueda.text sentenceIsSelect:YES];
            
        }
        [self.busquedaDiccionario reloadData];
        
    }
    else{
    // En este método haremos lo que creamos oportuno. Por lo general pasaremos a una vista detalle del elemento seleccionado. Haciendo uso de  UINavigationController que se puede ver en el correspondiente tutorial.
        [textBusqueda resignFirstResponder];
    NSDictionary *datoString = [self.valoresBusqueda objectAtIndex:indexPath.row];
    DatosViewController *loco = [[DatosViewController alloc] init];
    loco.palabra = [datoString objectForKey:@"word"];
    loco.meaning = [datoString objectForKey:@"meaning"];
    loco.summary = [datoString objectForKey:@"summary"];
    loco.tipo = self.textoDiccionario.currentTitle;
    loco.author = self.author;
    //loco.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentModalViewController:loco animated:YES];
            [self.textBusqueda resignFirstResponder];
    }
}



- (IBAction)irDiccionarios:(id)sender {
    DiccionariosViewController *vistaCreditos = [[DiccionariosViewController alloc]init];
    [self presentModalViewController:vistaCreditos animated:YES];
}

- (IBAction)irDescargas:(id)sender {
    JsonViewController *vistaDescargas =[[JsonViewController alloc]init];
    [self presentModalViewController:vistaDescargas animated:YES];
}
- (IBAction)buscadorSqlite:(id)sender {
    
    if ([self.textBusqueda.text isEqualToString:@""]) {
        NSLog(@"opcion vacia");
    }
    else{
        [self executeSentence:self.textBusqueda.text sentenceIsSelect:YES];
    }
    [self.busquedaDiccionario reloadData];
    
}
- (IBAction)cambiarDiccionario:(id)sender {
    if ([sender isSelected]) {
        [sender setSelected:NO];
        self.tabladeDiccionarios.hidden=YES;
    }
    else
    {
        [sender setSelected:YES];
        self.tabladeDiccionarios.hidden=NO;
    }
}

// Metodos necesarios para la lectura y escritura de datos ademas de obtener la ruta de el archivo Plist.
// Metodo de lectura archivo Plist en caso de no existir se crea solo una vez.
-(void)leer{
    
    self.datosDiccionario =[[NSMutableArray alloc]initWithContentsOfFile:[self ruta]];
    if (self.datosDiccionario==nil) {
        self.datosDiccionario = [[NSMutableArray alloc]init];
    }
    
}

// Metodo para guardar cambios al archivo Plist en disco.
-(void)guardar{
    [self.datosDiccionario writeToFile:[self ruta] atomically:YES];
}

// Funcion que retorna la ruta donde se almacena el archivo Plist.
-(NSString *)ruta{
    NSString *rutacarpeta =[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSAllDomainsMask, YES)objectAtIndex:0];
    return [rutacarpeta stringByAppendingPathComponent:@"Datos.plist"];
}

- (void)viewDidUnload {
    [self setBusquedaDiccionario:nil];
    [self setTextBusqueda:nil];
    [self setTextoDiccionario:nil];
    [self setTabladeDiccionarios:nil];
    [self setVistaSplah:nil];
    [super viewDidUnload];
}

-(void)viewWillAppear:(BOOL)animated{
    [self leer];
    [self.tabladeDiccionarios reloadData];
}
-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    
    [textField resignFirstResponder];
    return YES;
}

- (IBAction)irFavoritos:(id)sender {
    VistaViewController *ir = [[VistaViewController alloc]init];
    [self presentModalViewController:ir animated:YES];
}
- (IBAction)irCreditos:(id)sender {
    CreditosIIViewController *ir = [[CreditosIIViewController alloc]init];
    [self presentModalViewController:ir animated:YES];
}
@end
