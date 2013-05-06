//
//  gestionDescarga.m
//  SimiDic2
//
//  Created by KetanoLab on 20/03/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import "gestionDescarga.h"

@implementation gestionDescarga
@synthesize receivedata,nombrefile,FileSize,TotalByteSize,infoDiccionario,resumenDiccionario,nombreDiccionario,author,descripcionDicc,sw;
@synthesize textoAlerta;
-(void)descargaNombre: (NSString *)nombre Url:(NSString *)url nombreDicc:(NSString *)nombreDicci authoDicc:(NSString *)authorDic desc:(NSString *)descripcion indiceTabla:(int)indicetabla{
    [self leer];
    sw = [[NSMutableArray alloc]initWithObjects:@"YES",[NSNumber numberWithInt:indicetabla], nil];
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
    sw = [[NSMutableArray alloc]initWithObjects:@"NO",[NSNumber numberWithInt:-1], nil];
    [self guardar2];
    UIAlertView *rror = [[UIAlertView alloc]initWithTitle:@"Fallo Descarga" message:self.textoAlerta delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
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
    sw = [[NSMutableArray alloc]initWithObjects:@"NO",[NSNumber numberWithInt:-1], nil];
    [self guardar2];
    UIAlertView *alertaDescaga = [[UIAlertView alloc] initWithTitle:@"Descarga completa" message:self.textoAlerta delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    
    [alertaDescaga show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex==0) {
        NSLog(@"me seleccionaron a mi o de plano toy a la mierda");
        //ViewController *viewController = [[ViewController alloc]init];
    }
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

-(void)guardar2{
    [self.sw writeToFile:[self ruta2] atomically:YES];
}

// Funcion que retorna la ruta donde se almacena el archivo Plist.
-(NSString *)ruta2{
    NSString *rutacarpeta =[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSAllDomainsMask, YES)objectAtIndex:0];
    return [rutacarpeta stringByAppendingPathComponent:@"sw.plist"];
}
@end