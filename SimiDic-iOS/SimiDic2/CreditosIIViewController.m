//
//  CreditosIIViewController.m
//  SimiDic2
//
//  Created by KetanoLab on 01/04/13.
//  Copyright (c) 2013 KetanoLab. All rights reserved.
//

#import "CreditosIIViewController.h"

@interface CreditosIIViewController ()

@end

@implementation CreditosIIViewController
@synthesize primeraVista,segundaVista,terceraVista,cuartaVista,quintaVista,sextaVista,septimaVista,octavaVista,novenaVista,cadenaEstado;
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
    primeraVista.hidden = NO;
    cadenaEstado = @"Segunda";
    [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(cambiaVista) userInfo:nil repeats:YES];
    
}
-(void)cambiaVista{
    if ([cadenaEstado isEqualToString:@"Primera"]) {
        primeraVista.hidden = NO;
        segundaVista.hidden = YES;
        terceraVista.hidden = YES;
        cuartaVista.hidden = YES;
        quintaVista.hidden = YES;
        sextaVista.hidden = YES;
        septimaVista.hidden = YES;
        octavaVista.hidden = YES;
        novenaVista.hidden = YES;
        cadenaEstado = @"Segunda";
    }else if ([cadenaEstado isEqualToString:@"Segunda"]){
        primeraVista.hidden = YES;
        segundaVista.hidden = NO;
        terceraVista.hidden = YES;
        cuartaVista.hidden = YES;
        quintaVista.hidden = YES;
        sextaVista.hidden = YES;
        septimaVista.hidden = YES;
        octavaVista.hidden = YES;
        novenaVista.hidden = YES;
        cadenaEstado = @"Tercera";}
    else if ([cadenaEstado isEqualToString:@"Tercera"]){
        primeraVista.hidden = YES;
        segundaVista.hidden = YES;
        terceraVista.hidden = NO;
        cuartaVista.hidden = YES;
        quintaVista.hidden = YES;
        sextaVista.hidden = YES;
        septimaVista.hidden = YES;
        octavaVista.hidden = YES;
        novenaVista.hidden = YES;
        cadenaEstado = @"Cuarta";
    }else if ([cadenaEstado isEqualToString:@"Cuarta"]){
        primeraVista.hidden = YES;
        segundaVista.hidden = YES;
        terceraVista.hidden = YES;
        cuartaVista.hidden = NO;
        quintaVista.hidden = YES;
        sextaVista.hidden = YES;
        septimaVista.hidden = YES;
        octavaVista.hidden = YES;
        novenaVista.hidden = YES;
        cadenaEstado = @"Quinta";
    }else if ([cadenaEstado isEqualToString:@"Quinta"]){
        primeraVista.hidden = YES;
        segundaVista.hidden = YES;
        terceraVista.hidden = YES;
        cuartaVista.hidden = YES;
        quintaVista.hidden = NO;
        sextaVista.hidden = YES;
        septimaVista.hidden = YES;
        octavaVista.hidden = YES;
        novenaVista.hidden = YES;
        cadenaEstado = @"Sexta";
    }else if ([cadenaEstado isEqualToString:@"Sexta"]){
        primeraVista.hidden = YES;
        segundaVista.hidden = YES;
        terceraVista.hidden = YES;
        cuartaVista.hidden = YES;
        quintaVista.hidden = YES;
        sextaVista.hidden = NO;
        septimaVista.hidden = YES;
        octavaVista.hidden = YES;
        novenaVista.hidden = YES;
        cadenaEstado = @"Septimo";
    }else if ([cadenaEstado isEqualToString:@"Septimo"]){
        primeraVista.hidden = YES;
        segundaVista.hidden = YES;
        terceraVista.hidden = YES;
        cuartaVista.hidden = YES;
        quintaVista.hidden = YES;
        sextaVista.hidden = YES;
        septimaVista.hidden = NO;
        octavaVista.hidden = YES;
        novenaVista.hidden = YES;
        cadenaEstado = @"Octava";
    }else if ([cadenaEstado isEqualToString:@"Octava"]){
        primeraVista.hidden = YES;
        segundaVista.hidden = YES;
        terceraVista.hidden = YES;
        cuartaVista.hidden = YES;
        quintaVista.hidden = YES;
        sextaVista.hidden = YES;
        septimaVista.hidden = YES;
        octavaVista.hidden = NO;
        novenaVista.hidden = YES;
        cadenaEstado = @"Novena";
    }else if ([cadenaEstado isEqualToString:@"Novena"]){
        primeraVista.hidden = YES;
        segundaVista.hidden = YES;
        terceraVista.hidden = YES;
        cuartaVista.hidden = YES;
        quintaVista.hidden = YES;
        sextaVista.hidden = YES;
        septimaVista.hidden = YES;
        octavaVista.hidden = YES;
        novenaVista.hidden = NO;
        cadenaEstado = @"Primera";
    }
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidUnload {
    [self setPrimeraVista:nil];
    [self setSegundaVista:nil];
    [self setTerceraVista:nil];
    [self setCuartaVista:nil];
    [self setQuintaVista:nil];
    [self setSextaVista:nil];
    [self setSeptimaVista:nil];
    [self setOctavaVista:nil];
    [self setNovenaVista:nil];
    [self setVolver:nil];
    [super viewDidUnload];
}
- (IBAction)volverDiccionarios:(id)sender {
    [self dismissModalViewControllerAnimated:YES];
}
@end
