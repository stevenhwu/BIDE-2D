/*******************************************************************************
 * NormalDistribution.java
 * 
 * This file is part of BIDE-2D
 * 
 * Copyright (C) 2012 Steven Wu
 * 
 * BIDE-2D is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BIDE-2D is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with BIDE-2D.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package bide.math;


import org.apache.commons.math3.random.RandomDataImpl;


public class NormalDistribution implements Distribution {

	private final static double MATH_SQRT_2 = Math.sqrt(2.0);
	private final static double MATH_SQRT_2PI = Math.sqrt(2.0 * Math.PI);
	private double m;
	private double sd;
	private double upperLim;
	private double lowerLim;
	private static RandomDataImpl rnorm = new RandomDataImpl();

	public NormalDistribution(double mean, double sd) {
		this.m = mean;
		this.sd = sd;
	}

	public NormalDistribution() {
	}

	@Override
	public double mean() {
		return m;
	}

	@Override
	public double variance() {
		return sd * sd;
	}

	public double getMean() {
		return m;
	}

	public void setMean(double value) {
		m = value;
	}

	public double getSD() {
		return sd;
	}

	public void setSD(double value) {
		sd = value;
	}

	public void setParam(double m, double sd) {

		this.m = m;
		this.sd = sd;
	}

	public void setBothLim(double lower, double upper) {
		this.lowerLim = lower;
		this.upperLim = upper;
	}

	public double getLowerLim() {
		return lowerLim;
	}

	public void setLowerLim(double lowerLim) {
		this.lowerLim = lowerLim;
	}

	public double getUpperLim() {
		return upperLim;
	}

	public void setUpperLim(double upperLim) {
		this.upperLim = upperLim;
	}
    
	/**
     * quantiles (=inverse cumulative density function)
     *
     * @param z  argument
     * @param m  mean
     * @param sd standard deviation
     * @return icdf at z
     */
    public static double quantile(double z, double m, double sd) {
        return m + Math.sqrt(2.0) * sd * ErrorFunction.inverseErf(2.0 * z - 1.0);
    }

    
	public static double pdf(double x, double m, double sd) {
		double a = 1.0 / (MATH_SQRT_2PI * sd);
		double b = -(x - m) * (x - m) / (2.0 * sd * sd);

		return a * Math.exp(b);
	}

	@Override
	public double pdf(double x) {

		return pdf(x, m, sd);
	}

	public static double logPdf(double x, double m, double sd) {
		double a = 1.0 / (MATH_SQRT_2PI * sd);
		double b = -(x - m) * (x - m) / (2.0 * sd * sd);
		return Math.log(a) + b;
	}

	@Override
	public double logPdf(double x) {

		return logPdf(x, m, sd);
	}
	

	public double cdf(double x) {

		return cdf(x, m, sd);
	}

	public static double cdf(double x, double m, double sd) {

		double a = (x - m) / (MATH_SQRT_2 * sd);
		if (a > 100) {
			return 1;
		} else if (a < -100) {
			return 0;
		} else {
			return 0.5 * (1.0 + ErrorFunction.erf(a));
		}

		/*
		 * NormalDistributionImpl nd = new NormalDistributionImpl(m, sd); double
		 * cnorm = 0; try { cnorm = nd.cumulativeProbability(x); } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
	}

	/*
	 * pval<-.Internal(dnorm(x[x<upB&x>lowB],mean,sd,log=T))- log(
	 * .Internal(pnorm
	 * (upB,mean,sd,lower.tail=T,log.p=F))-.Internal(pnorm(lowB,mean
	 * ,sd,lower.tail=T,log.p=F)) ) }
	 */
	public static double logPdfBT(double x, double m, double sd, double lLim, double uLim) {

		double dnorm = 0;

		try {
			double limit = Math.log(NormalDistribution.cdf(uLim, m, sd)
					- NormalDistribution.cdf(lLim, m, sd));

			if (x > lLim && x < uLim) {
				dnorm = NormalDistribution.logPdf(x, m, sd) - limit;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return dnorm;
	}
	
	public double logPdfBT(double x) {

		return logPdfBT(x, m, sd, lowerLim, upperLim);
	}

	// public double logPdfBT(double x) {
	//
	// double dnorm = 0;
	//		
	// try {
	// double limit = Math.log(cdf(upperLim) - cdf(lowerLim));
	//
	// if (x > lowerLim && x < upperLim) {
	// dnorm = logPdf(x) - limit;
	// }
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	// return dnorm;
	// }

	/**
	 * Normal distribution truncated at both side
	 * 
	 * @param x
	 * @return
	 */
	public static double[] logPdfBT(double[] x,  double m, double sd, double lLim, double uLim) {
		int n = x.length;
		if (n == 0) {
			double[] dnorm = { 0 };
			return dnorm;
		} else {
			double[] dnorm = new double[n];
			try {
				double limit = Math.log(NormalDistribution.cdf(uLim, m, sd)
						- NormalDistribution.cdf(lLim, m, sd));
				double d;
				for (int i = 0; i < n; i++) {
					d = x[i];
					if (d > lLim && d < uLim) {
						dnorm[i] = NormalDistribution.logPdf(d, m, sd) - limit;
					} else {
						dnorm[i] = 0;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dnorm;
		}
	}
	
	public double[] logPdfBT(double... x) {
		return logPdfBT(x, m, sd, lowerLim, upperLim);
	}

	/**
	 * lower truncated log pdf for normal distribution
	 * 
	 * @param x
	 * @param lowerLim
	 * @return
	 */
	// public double logPdfLT (double x, double lowerLim) {
	// double dnorm = 0;
	// if(x>lowerLim){
	// try{
	// dnorm =logPdf(x)-Math.log(1-cdf(lowerLim));
	// }
	// catch (Exception e) {
	// }
	// }
	// return dnorm;
	//		
	// }
	public double[] logPdfLT(double... x) {
		int n = x.length;
		double[] dnorm = new double[n];

		try {
			double limit = Math.log(1 - cdf(lowerLim));
			double d;
			for (int i = 0; i < n; i++) {
				d = x[i];
				if (d > lowerLim) {
					dnorm[i] = logPdf(d) - limit;
				} else {
					dnorm[i] = 0;
				}
			}
		} catch (Exception e) {
		}
		return dnorm;
	}

	public double randomDist() {

		return randomDist(m, sd);
	}

	public static double randomDist(double m, double sd) {

		return rnorm.nextGaussian(m, sd);
	}


    /** A more accurate and faster implementation of the cdf (taken from function pnorm in the R statistical language)
     * This implementation has discrepancies depending on the programming language and system architecture
     * In Java, returned values become zero once z reaches -37.5193 exactly on the machine tested
     * In the other implementation, the returned value 0 at about z = -8
     * In C, this 0 value is reached approximately z = -37.51938
     *
     * Will later need to be optimised for BEAST
     *
     * @param x     argument
     * @param mu    mean
     * @param sigma standard deviation
     * @param log_p is p logged
     * @return cdf at x
     */
    public static double cdf(double x, double mu, double sigma, boolean log_p) {
        boolean i_tail=false;
        double p, cp = Double.NaN;
        
        if(Double.isNaN(x) || Double.isNaN(mu) || Double.isNaN(sigma)) {
            return Double.NaN;
        }
        if(Double.isInfinite(x) && mu == x) { /* x-mu is NaN */
            return Double.NaN;
        }
        if (sigma <= 0) {
            if(sigma < 0) {
                return Double.NaN;
            }
            return (x < mu) ? 0.0 : 1.0;
        }
        p = (x - mu) / sigma;
        if(Double.isInfinite(p)) {
            return (x < mu) ? 0.0 : 1.0;
        }
        x = p;
        if(Double.isNaN(x)) {
            return Double.NaN;
        }

        double xden, xnum, temp, del, eps, xsq, y;
        int i;
        boolean lower, upper;
        eps = DBL_EPSILON * 0.5;
        lower = !i_tail;
        upper = i_tail;

        y = Math.abs(x);
        if (y <= 0.67448975) { /* Normal.quantile(3/4, 1, 0) = 0.67448975 */
            if (y > eps) {
                xsq = x * x;
                xnum = a[4] * xsq;
                xden = xsq;
                for (i = 0; i < 3; i++) {
                    xnum = (xnum + a[i]) * xsq;
                    xden = (xden + b[i]) * xsq;
                }
            }
            else {
                xnum = xden = 0.0;
            }
            temp = x * (xnum + a[3]) / (xden + b[3]);
            if(lower) {
                p = 0.5 + temp;
            }
            if(upper) {
                cp = 0.5 - temp;
            }
            if(log_p) {
                if(lower) {
                    p = Math.log(p);
                }
                if(upper) {
                    cp = Math.log(cp);
                }
            }
        }


        else if (y <= M_SQRT_32) {
            /* Evaluate pnorm for 0.67448975 = Normal.quantile(3/4, 1, 0) < |x| <= sqrt(32) ~= 5.657 */

            xnum = c[8] * y;
            xden = y;
            for (i = 0; i < 7; i++) {
                xnum = (xnum + c[i]) * y;
                xden = (xden + d[i]) * y;
            }
            temp = (xnum + c[7]) / (xden + d[7]);

            //do_del(y);
            //swap_tail;
            //#define do_del(X)							\
            xsq = ((int) (y * CUTOFF)) * 1.0 / CUTOFF;
            del = (y - xsq) * (y + xsq);
            if(log_p) {
                p = (-xsq * xsq * 0.5) + (-del * 0.5) + Math.log(temp);
                if((lower && x > 0.0) || (upper && x <= 0.0)) {
                  cp = Math.log(1.0-Math.exp(-xsq * xsq * 0.5) * Math.exp(-del * 0.5) * temp);
                }
            }
            else {
                p = Math.exp(-xsq * xsq * 0.5) * Math.exp(-del * 0.5) * temp;
                cp = 1.0 - p;
            }
            //#define swap_tail						\
            if (x > 0.0) {
                temp = p;
                if(lower) {
                    p = cp;
                }
                cp = temp;
            }
        }
        /* else	  |x| > sqrt(32) = 5.657 :
         * the next two case differentiations were really for lower=T, log=F
         * Particularly	 *not*	for  log_p !
         * Cody had (-37.5193 < x  &&  x < 8.2924) ; R originally had y < 50
         * Note that we do want symmetry(0), lower/upper -> hence use y
         */
        else if(log_p || (lower && -37.5193 < x  &&  x < 8.2924)
            || (upper && -8.2924  < x  &&  x < 37.5193)) {

            /* Evaluate pnorm for x in (-37.5, -5.657) union (5.657, 37.5) */
            xsq = 1.0 / (x * x);
            xnum = p_[5] * xsq;
            xden = xsq;
            for (i = 0; i < 4; i++) {
                xnum = (xnum + p_[i]) * xsq;
                xden = (xden + q[i]) * xsq;
            }
            temp = xsq * (xnum + p_[4]) / (xden + q[4]);
            temp = (M_1_SQRT_2PI - temp) / y;

            //do_del(x);
            xsq = ((int) (x * CUTOFF)) * 1.0 / CUTOFF;
            del = (x - xsq) * (x + xsq);
            if(log_p) {
                p = (-xsq * xsq * 0.5) + (-del * 0.5) + Math.log(temp);
                if((lower && x > 0.0) || (upper && x <= 0.0)) {
                  cp = Math.log(1.0-Math.exp(-xsq * xsq * 0.5) * Math.exp(-del * 0.5) * temp);
                }
            }
            else {
                p = Math.exp(-xsq * xsq * 0.5) * Math.exp(-del * 0.5) * temp;
                cp = 1.0 - p;
            }
            //swap_tail;
            if (x > 0.0) {
                temp = p;
                if(lower) {
                    p = cp;
                }
                cp = temp;
            }
        }
        else { /* no log_p , large x such that probs are 0 or 1 */
            if(x > 0) {
                p = 1.0;
                cp = 0.0;
            }
            else {
                p = 0.0;
                cp = 1.0;
            }
        }
        return p;

    }

    // Private

    private static final double[] a = {
        2.2352520354606839287,
        161.02823106855587881,
        1067.6894854603709582,
        18154.981253343561249,
        0.065682337918207449113
    };
    private static final double[] b = {
        47.20258190468824187,
        976.09855173777669322,
        10260.932208618978205,
        45507.789335026729956
    };
    private static final double[] c = {
        0.39894151208813466764,
        8.8831497943883759412,
        93.506656132177855979,
        597.27027639480026226,
        2494.5375852903726711,
        6848.1904505362823326,
        11602.651437647350124,
        9842.7148383839780218,
        1.0765576773720192317e-8
    };
    private static final double[] d = {
        22.266688044328115691,
        235.38790178262499861,
        1519.377599407554805,
        6485.558298266760755,
        18615.571640885098091,
        34900.952721145977266,
        38912.003286093271411,
        19685.429676859990727
    };
    private static final double[] p_ = {
        0.21589853405795699,
        0.1274011611602473639,
        0.022235277870649807,
        0.001421619193227893466,
        2.9112874951168792e-5,
        0.02307344176494017303
    };
    private static final double[] q = {
        1.28426009614491121,
        0.468238212480865118,
        0.0659881378689285515,
        0.00378239633202758244,
        7.29751555083966205e-5
    };

    private static final int CUTOFF = 16; /* Cutoff allowing exact "*" and "/" */

    private static final double M_SQRT_32 = 5.656854249492380195206754896838; /* The square root of 32 */
    private static final double M_1_SQRT_2PI =  0.398942280401432677939946059934;
    private static final double DBL_EPSILON = 2.2204460492503131e-016;

}
