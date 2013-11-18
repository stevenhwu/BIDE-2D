BIDE-2D
=======

BIDE-2D: Bayesian Inference of Differential Expression in 2D-PAGE

This software is based on the article
 - A Bayesian model for classifying all differentially expressed proteins simultaneously in 2D PAGE gels
Steven H Wu*, Michael A Black, Robyn A North and Allen G Rodrigo
http://www.biomedcentral.com/1471-2105/13/137

Abstract
Background
Two-dimensional polyacrylamide gel electrophoresis (2D PAGE) is commonly used to identify differentially expressed proteins under two or more experimental or observational conditions. Wu et al (2009) developed a univariate probabilistic model which was used to identify differential expression between Case and Control groups, by applying a Likelihood Ratio Test (LRT) to each protein on a 2D PAGE. In contrast to commonly used statistical approaches, this model takes into account the two possible causes of missing values in 2D PAGE: either (1) the non-expression of a protein; or (2) a level of expression that falls below the limit of detection.

Results
We develop a global Bayesian model which extends the previously described model. Unlike the univariate approach, the model reported here is able treat all differentially expressed proteins simultaneously. Whereas each protein is modelled by the univariate likelihood function previously described, several global distributions are used to model the underlying relationship between the parameters associated with individual proteins. These global distributions are able to combine information from each protein to give more accurate estimates of the true parameters. In our implementation of the procedure, all parameters are recovered by Markov chain Monte Carlo (MCMC) integration. The 95% highest posterior density (HPD) intervals for the marginal posterior distributions are used to determine whether differences in protein expression are due to differences in mean expression intensities, and/or differences in the probabilities of expression.

Conclusions
Simulation analyses showed that the global model is able to accurately recover the underlying global distributions, and identify more differentially expressed proteins than the simple application of a LRT. Additionally, simulations also indicate that the probability of incorrectly identifying a protein as differentially expressed (i.e., the False Discovery Rate) is very low. 
