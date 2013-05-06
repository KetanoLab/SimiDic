//
//  JsonViewController.m
//  SimiDic2
//
//  Created by KetanoLab on 12/03/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import "JsonViewController.h"
#import "ViewController.h"
#import "gestionDescarga.h"
#import "sqlite3.h"

@interface JsonViewController ()

@property (nonatomic,strong) NSMutableArray *valoresJson;
@property (nonatomic,strong) NSMutableArray *ArrayDiccionariosRestantes;

@end

@implementation JsonViewController
@synthesize tablaDescargas,valoresJson,index;
@synthesize infoDiccionario,ArrayDiccionariosRestantes,labelNomasDescargas;
@synthesize swDescarga;
@synthesize receivedata,nombrefile,FileSize,TotalByteSize,resumenDiccionario,nombreDiccionario,author,descripcionDicc;
@synthesize textoAlerta;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self leer];
    [self leer2];
    NSLog(@"%@",swDescarga);
    // Verificar la connecion a internet a travez de la siguiente formacion de una cadena usando el contenido de una direccion url.
    NSString *internetConnection = [[NSString alloc] initWithContentsOfURL:[NSURL URLWithString:@"http://www.illa-a.org/simidic/simidic.json"] encoding:NSUTF8StringEncoding error:nil];
    self.index = 100;
    // Verificar la connecion.
    if (internetConnection) {
        [self mostrarTabla:internetConnection];
    }
    else{
        // Generar un nuevo alertView que informa al usuario que la aplicacion no tiene internet y lo regresa a la pantalla principal.
        UIAlertView *alertNoInternet = [[UIAlertView alloc] initWithTitle:@"Alerta" message:@"La aplicacion no tiene internet no podra descargar ningun Diccionario" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alertNoInternet show];
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex==0) {
        [self dismissModalViewControllerAnimated:YES];
    }
    else{
        JsonViewController *view = [[JsonViewController alloc]init];
        [self presentModalViewController:view animated:YES];
    }
}

-(void)mostrarTabla:(NSString *)jsonString{
    NSData *jsonData = [ [NSString stringWithFormat:@"{\"cosasraras\":%@}",jsonString] dataUsingEncoding:NSUTF8StringEncoding];
    NSDictionary *json = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:nil];
    valoresJson = [[NSMutableArray alloc]initWithArray:[json objectForKey:@"cosasraras"]];
    ArrayDiccionariosRestantes = [[NSMutableArray alloc]init];
    NSLog(@"%@",valoresJson);
    int sw;
    for (int i=0; i<[valoresJson count]; i++) {
        json = [valoresJson objectAtIndex:i];
        NSDictionary *aux = [[NSDictionary alloc]init];
        sw = 0;
        for (int j=0; j<[infoDiccionario count]; j++) {
            aux = [infoDiccionario objectAtIndex:j];
            if ([[json objectForKey:@"name"]isEqualToString:[aux objectForKey:@"nombreDicc"]]) {
                sw = 1;
            }
        }
        if (sw == 0) {
            [ArrayDiccionariosRestantes addObject:[valoresJson objectAtIndex:i]];
        }
    
    }
    if ([ArrayDiccionariosRestantes count]==0) {
        labelNomasDescargas.text = @"No new dictionaries to download, please try later.";
        labelNomasDescargas.hidden = NO;
        tablaDescargas.hidden = YES;
    }
    [tablaDescargas reloadData];
}
// Método necesario para devolver el número de secciones de la tabla, por lo general será siempre 1..
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}
// Necesario para indicar el número de filas de la tabla, esto suele ir ligado al tamaño de un array de elementos a mostrar..
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [ArrayDiccionariosRestantes count];
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    // Normalmente recuperaremos del array, según la posicion de la fila..
    NSDictionary *datoString = [ArrayDiccionariosRestantes objectAtIndex:indexPath.row];
    // Creamos la celda (o fila).
    UITableViewCell *cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"Cell"];
    // Y le establecemos el texto de nuestro dato a una de las filas.
    cell.textLabel.text = [datoString objectForKey:@"name"];
    if ([[swDescarga objectAtIndex:0]isEqualToString:@"YES"]) {
        if ([[swDescarga objectAtIndex:1]intValue] == indexPath.row) {
            cell.detailTextLabel.text = @"Descargando......";
        }else{
            cell.detailTextLabel.text = [datoString objectForKey:@"size"];
        }
    [cell setUserInteractionEnabled:NO];
    }
    else{
    cell.detailTextLabel.text = [datoString objectForKey:@"size"];
    [cell setUserInteractionEnabled:YES];
    }
    // Y finalizamos devolviendo la celda
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    // En este método haremos lo que creamos oportuno. Por lo general pasaremos a una vista detalle del elemento seleccionado. Haciendo uso de  UINavigationController que se puede ver en el correspondiente tutorial.
    NSDictionary *datoString = [ArrayDiccionariosRestantes objectAtIndex:indexPath.row];
    [self descargaNombre:[datoString objectForKey:@"file"] Url:[datoString objectForKey:@"url"] nombreDicc:[datoString objectForKey:@"name" ] authoDicc:[datoString objectForKey:@"author" ] desc:[datoString objectForKey:@"description" ] indiceTabla:indexPath.row];
    self.index = indexPath.row;
    UIAlertView *alertaNoMasdescargas = [[UIAlertView alloc]initWithTitle:@"Descargando" message:@"Se esta descargando un Diccionario no podra descargar mas" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    [alertaNoMasdescargas show];
    [self leer2];
    NSLog(@"%@",swDescarga);
    [tablaDescargas reloadData];
   }

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)volverPrincipal:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
}

- (void)viewDidUnload {
    [self setTablaDescargas:nil];
    [self setLabelNomasDescargas:nil];
    [super viewDidUnload];
}
-(void)leer{
    
    self.infoDiccionario =[[NSMutableArray alloc]initWithContentsOfFile:[self ruta]];
    if (self.infoDiccionario==nil) {
        self.infoDiccionario = [[NSMutableArray alloc]init];
    }
    
}

// Metodo para guardar cambios al archivo Plist en disco.
-(void)guardar{
    [self.infoDiccionario writeToFile:[self ruta] atomically:YES];
}

// Funcion que retorna la ruta donde se almacena el archivo Plist.
-(NSString *)ruta{
    NSString *rutacarpeta =[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSAllDomainsMask, YES)objectAtIndex:0];
    return [rutacarpeta stringByAppendingPathComponent:@"Datos.plist"];
}

#pragma mark

-(void)leer2{
    
    self.swDescarga =[[NSMutableArray alloc]initWithContentsOfFile:[self ruta2]];
    if (self.swDescarga==nil) {
        self.swDescarga = [[NSMutableArray alloc]initWithObjects:@"NO",[NSNumber numberWithInt:-1],nil];
    }
}

// Funcion que retorna la ruta donde se almacena el archivo Plist.
-(NSString *)ruta2{
    NSString *rutacarpeta =[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSAllDomainsMask, YES)objectAtIndex:0];
    return [rutacarpeta stringByAppendingPathComponent:@"sw.plist"];
}

-(void)guardar2{
    [self.swDescarga writeToFile:[self ruta2] atomically:YES];
}


-(void)descargaNombre: (NSString *)nombre Url:(NSString *)url nombreDicc:(NSString *)nombreDicci authoDicc:(NSString *)authorDic desc:(NSString *)descripcion indiceTabla:(int)indicetabla{
    [self leer];
    swDescarga = [[NSMutableArray alloc]initWithObjects:@"YES",[NSNumber numberWithInt:indicetabla], nil];
    [self guardar2];
    // Creacion del elemento request que se encargara de la descarga del elemento.
    NSURLRequest *request = [[NSURLRequest alloc] initWithURL:[NSURL URLWithString:url] cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:20];
    // Creacion del elemento que establece la conexion con la direccion situada y podemos ver si esta ha tenido exito o no.
    NSURLConnection *theConnection=[[NSURLConnection alloc] initWithRequest:request delegate:self];
    if (theConnection) {
        // Create the NSMutableData to hold the received data.
        // receivedData is an instance variable declared elsewhere.
        self.receivedata = [NSMutableData data];
    } else {
        // Inform the user that the connection failed.
    }
    self.nombrefile = nombre;
    self.nombrefile=[self.nombrefile substringToIndex:([self.nombrefile length]-2)];
    self.nombrefile =[[NSString alloc] initWithFormat:@"%@sqlite",self.nombrefile];
    self.author = authorDic;
    self.nombreDiccionario = nombreDicci;
    self.descripcionDicc = descripcion;
    textoAlerta = [[NSString alloc]initWithFormat:@"La descarga del diccionario : %@",nombreDicci];
}

// Metodos delegados propios de NSURLCONNECTION


- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    // This method is called when the server has determined that it
    // has enough information to create the NSURLResponse.
    
    // It can be called multiple times, for example in the case of a
    // redirect, so each time we reset the data.
    
    // receivedData is an instance variable declared elsewhere.
    self.FileSize = response.expectedContentLength;
    NSLog(@"%@",response.suggestedFilename);
    NSLog(@"veamos de cuantos bits hablamos jejeje%i",self.FileSize);
    [self.receivedata setLength:0];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    // Append the new data to receivedData.
    // receivedData is an instance variable declared elsewhere.
    self.TotalByteSize += [data length];
    NSLog(@"veamos si he recibido los datos de forma adecuada %f",TotalByteSize);
    [self.receivedata appendData:data];
}

- (void)connection:(NSURLConnection *)connection
  didFailWithError:(NSError *)error
{
    // inform the user
    NSLog(@"Connection failed! Error - %@ %@",
          [error localizedDescription],
          [[error userInfo] objectForKey:NSURLErrorFailingURLStringErrorKey]);
    swDescarga = [[NSMutableArray alloc]initWithObjects:@"NO",[NSNumber numberWithInt:-1], nil];
    [self guardar2];
    UIAlertView *rror = [[UIAlertView alloc]initWithTitle:@"Fallo Descarga" message:self.textoAlerta delegate:self cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
    [rror show];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    // t[self createEditableCopyOfDatabaseIfNeeded];
    // do something with the data
    // receivedData is declared as a method instance elsewhere
    // Ruta donde actualmente se encuentra la base de datos cajeros.sqlite.
    NSString *documentsPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) lastObject];
    // Sobreescribir la base de datos en la ruta anterior, de manera automatica si los nombres de los objetos descargados son los mismos.
    NSString *filePath = [documentsPath stringByAppendingPathComponent:self.nombrefile];
    [self.receivedata writeToFile:filePath atomically:YES];
    NSLog(@"Succeeded! Received %d bytes of data",[self.receivedata length]);
    NSDictionary *temporalDicconario = [[NSDictionary alloc] initWithObjectsAndKeys:self.nombrefile,@"nombrefile",self.author,@"author",self.descripcionDicc,@"descripcion",self.nombreDiccionario,@"nombreDicc", nil];
    [self.infoDiccionario addObject:temporalDicconario];
    [self guardar];
    swDescarga = [[NSMutableArray alloc]initWithObjects:@"NO",[NSNumber numberWithInt:-1], nil];
    [self guardar2];
    UIAlertView *alertaDescaga = [[UIAlertView alloc] initWithTitle:@"Descarga completa" message:self.textoAlerta delegate:self cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
    
    [alertaDescaga show];
}





@end
