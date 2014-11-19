// FRANCOIS Remy

basse = zeros(40,1);
moyenne = zeros(40,1);
elevee = zeros(40,1);
// initialiser les ensembles flous
for i=1:11
    basse(i,1) = 1;
end
for i=11:21
    basse(i,1) = 1 - (i-10)/10;
end
for i=21:40
    basse(i,1) = 0;
end


for i=1:11
    moyenne(1,1) = 0;
end
for i=11:21
    moyenne(i,1) = (i-10)/10;
end
for i=21:30
    moyenne(i,1) = 1- (i-20)/10;
end
for i=30:40
    basse(i,1) = 0;
end

for i=1:21
    elevee(i,1) = 0;
end
for i=21:31
    elevee(i,1) = (i-20)/10;
end
for i=31:40
    elevee(i,1) = 1;
end

// calculer ensembles basse ou moyenne
bmMin = zeros(40,1)
for i=1:40
    if basse(i,1) > 0 | moyenne(i,1)> 0  then
        bmMin(i,1) = min(basse(i,1), moyenne(i,1));
    end
end

bmMax = zeros(40,1)
for i=1:40
    if basse(i,1) > 0 | moyenne(i,1)> 0  then
        bmMax(i,1) = max(basse(i,1), moyenne(i,1));
    end
end

//plot2d([basse moyenne elevee bmMin], style=[2,3,5,4]) ;
plot2d([basse moyenne elevee bmMax], style=[2,3,5,6]) ;