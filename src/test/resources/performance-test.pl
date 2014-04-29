#!/usr/bin/perl

use strict;
use warnings;
$|++;

use LWP::UserAgent;

my $verbose = grep /^(-v|--verbose)$/, @ARGV;
my $num_iterations = 10000;

my $ua = LWP::UserAgent->new;
my $base_url = 'http://ipmc-dev3.be-md.ncbi.nlm.nih.gov:9999/pmc-citation-service/?style=ieee&ids=PMC';

my $pmcid = '1000';

my $start_time = time;
my $last_message = $start_time;
my $now;
print "Requests | Time elapsed | Average time/request | Average requests/sec\n";
for (my $i = 0; $i < $num_iterations; ++$i) {
    my $req_url = $base_url . $pmcid++;
    my $req = HTTP::Request->new(GET => $req_url);
    #print "req_url = $req_url\n";
    my $resp = $ua->request($req);
    if (!$resp->is_success) {
        die "Request failed: " . $resp->status_line;
    }
    if ($verbose) { print $resp->content; }
    $now = time;
    if ($now - $last_message >= 5) {
        output_time($i + 1);
        $last_message = $now;
    }
}
output_time($num_iterations);
exit 0;


sub output_time {
    my $iter_num = shift;
    my $delta = $now - $start_time;
    my $time_req = $delta / $iter_num;
    my $req_sec = $iter_num / $delta;
    printf("%7d  |    %6d    |        %3.5f       |      %4d\n",
           $iter_num, $delta, $time_req, $req_sec);
}

